import gzip
import os
import time
import unicodedata
import ijson
import requests

RUTA_DATOS = os.path.join(os.path.dirname(__file__), "datos_ubicaciones.json.gz")
URL_API_BASE = os.getenv("API_BASE_URL", "https://portal-fidelizacion-gco-api-eaczavaqb7dsg2f0.brazilsouth-01.azurewebsites.net/api").rstrip("/")
URL_API_PAISES = f"{URL_API_BASE}/paises"
URL_API_DEPARTAMENTOS = f"{URL_API_BASE}/departamentos"
URL_API_CIUDADES = f"{URL_API_BASE}/ciudades"
TIMEOUT = 60
REINTENTOS = 3

def normalizar_nombre(nombre):
    return unicodedata.normalize("NFKC", nombre.strip()).casefold()

def indexar_por_nombre(registros):
    return {normalizar_nombre(registro["nombre"]): registro for registro in registros}

def obtener_lista(session, url, params=None):
    respuesta = session.get(url, params=params, timeout=TIMEOUT)
    respuesta.raise_for_status()
    return respuesta.json()

def enviar_post(session, url, datos):
    ultimo_error = None
    for intento in range(1, REINTENTOS + 1):
        try:
            respuesta = session.post(url, json=datos, timeout=TIMEOUT)
            respuesta.raise_for_status()
            return respuesta
        except requests.RequestException as error:
            ultimo_error = error
            if intento < REINTENTOS:
                espera = intento * 2
                print(f"Error enviando registro. Reintento {intento}/{REINTENTOS} en {espera}s - {error}")
                time.sleep(espera)
    raise ultimo_error

def obtener_o_crear(session, url, datos, registros_por_nombre):
    clave = normalizar_nombre(datos["nombre"])
    registro_encontrado = registros_por_nombre.get(clave)
    if registro_encontrado:
        return registro_encontrado, False
    respuesta = enviar_post(session, url, datos)
    registro_creado = respuesta.json()
    registros_por_nombre[normalizar_nombre(registro_creado["nombre"])] = registro_creado
    return registro_creado, True

def cargar_ubicaciones():
    if not os.path.exists(RUTA_DATOS):
        print("No se encontró el archivo de ubicaciones:")
        print(RUTA_DATOS)
        return

    totales = {
        "paises_creados": 0,
        "paises_existentes": 0,
        "departamentos_creados": 0,
        "departamentos_existentes": 0,
        "ciudades_creadas": 0,
        "ciudades_existentes": 0,
        "errores": 0,
    }

    with requests.Session() as session:
        session.headers.update({"Accept": "application/json", "Content-Type": "application/json"})
        print("Backend utilizado:")
        print(URL_API_BASE)
        print()
        print("Consultando países existentes...")
        paises_guardados = obtener_lista(session, URL_API_PAISES)
        paises_por_nombre = indexar_por_nombre(paises_guardados)
        print("Leyendo datos geográficos locales...")

        with gzip.open(RUTA_DATOS, "rb") as archivo_gzip:
            paises = ijson.items(archivo_gzip, "item")

            for pais in paises:
                nombre_pais = pais["name"]
                departamentos = pais.get("states") or []
                datos_pais = {"nombre": nombre_pais, "activo": True}
                print()
                print("Procesando país:", nombre_pais)
                print("Divisiones administrativas:", len(departamentos))

                try:
                    pais_guardado, pais_creado = obtener_o_crear(session, URL_API_PAISES, datos_pais, paises_por_nombre)
                except requests.RequestException as error:
                    totales["errores"] += 1
                    print("Error procesando país:", nombre_pais, "-", error)
                    continue

                id_pais = pais_guardado["id"]

                if pais_creado:
                    totales["paises_creados"] += 1
                    print("País creado:", nombre_pais)
                else:
                    totales["paises_existentes"] += 1
                    print("País ya existente:", nombre_pais)

                try:
                    departamentos_guardados = obtener_lista(session, URL_API_DEPARTAMENTOS, params={"idPais": id_pais})
                except requests.RequestException as error:
                    totales["errores"] += 1
                    print("Error consultando departamentos de:", nombre_pais, "-", error)
                    continue

                departamentos_por_nombre = indexar_por_nombre(departamentos_guardados)
                departamentos_creados_pais = 0
                departamentos_existentes_pais = 0
                ciudades_creadas_pais = 0
                ciudades_existentes_pais = 0

                for departamento in departamentos:
                    nombre_departamento = departamento["name"]
                    ciudades = departamento.get("cities") or []
                    datos_departamento = {"nombre": nombre_departamento, "activo": True, "idPais": id_pais}

                    try:
                        departamento_guardado, departamento_creado = obtener_o_crear(session, URL_API_DEPARTAMENTOS, datos_departamento, departamentos_por_nombre)
                    except requests.RequestException as error:
                        totales["errores"] += 1
                        print("Error procesando división:", nombre_departamento, "-", error)
                        continue

                    id_departamento = departamento_guardado["id"]

                    if departamento_creado:
                        totales["departamentos_creados"] += 1
                        departamentos_creados_pais += 1
                    else:
                        totales["departamentos_existentes"] += 1
                        departamentos_existentes_pais += 1

                    try:
                        ciudades_guardadas = obtener_lista(session, URL_API_CIUDADES, params={"idDepartamento": id_departamento})
                    except requests.RequestException as error:
                        totales["errores"] += 1
                        print("Error consultando ciudades de:", nombre_departamento, "-", error)
                        continue

                    nombres_existentes = {normalizar_nombre(ciudad["nombre"]) for ciudad in ciudades_guardadas}
                    nombres_fuente = set()
                    ciudades_creadas_departamento = 0
                    ciudades_existentes_departamento = 0

                    for ciudad in ciudades:
                        nombre_ciudad = ciudad.get("name")

                        if nombre_ciudad is None or not nombre_ciudad.strip():
                            totales["errores"] += 1
                            print("Ciudad sin nombre en:", nombre_departamento)
                            continue

                        clave = normalizar_nombre(nombre_ciudad)

                        if clave in nombres_fuente:
                            continue

                        nombres_fuente.add(clave)

                        if clave in nombres_existentes:
                            ciudades_existentes_departamento += 1
                            continue

                        datos_ciudad = {"nombre": nombre_ciudad, "activo": True, "idDepartamento": id_departamento}

                        try:
                            respuesta = enviar_post(session, URL_API_CIUDADES, datos_ciudad)
                            ciudad_creada = respuesta.json()
                            nombres_existentes.add(normalizar_nombre(ciudad_creada["nombre"]))
                            ciudades_creadas_departamento += 1
                        except requests.RequestException as error:
                            totales["errores"] += 1
                            print("Error procesando ciudad:", nombre_ciudad, "-", error)

                    totales["ciudades_creadas"] += ciudades_creadas_departamento
                    totales["ciudades_existentes"] += ciudades_existentes_departamento
                    ciudades_creadas_pais += ciudades_creadas_departamento
                    ciudades_existentes_pais += ciudades_existentes_departamento

                    print(nombre_departamento, "- ciudades nuevas:", ciudades_creadas_departamento, "- existentes:", ciudades_existentes_departamento)

                print("Resumen país:", nombre_pais)
                print("Departamentos nuevos:", departamentos_creados_pais)
                print("Departamentos existentes:", departamentos_existentes_pais)
                print("Ciudades nuevas:", ciudades_creadas_pais)
                print("Ciudades existentes:", ciudades_existentes_pais)

    print()
    print("========================================")
    print("CARGA FINALIZADA")
    print("========================================")
    print("Países creados:", totales["paises_creados"])
    print("Países existentes:", totales["paises_existentes"])
    print("Departamentos creados:", totales["departamentos_creados"])
    print("Departamentos existentes:", totales["departamentos_existentes"])
    print("Ciudades creadas:", totales["ciudades_creadas"])
    print("Ciudades existentes:", totales["ciudades_existentes"])
    print("Errores:", totales["errores"])

if __name__ == "__main__":
    try:
        cargar_ubicaciones()
    except requests.RequestException as error:
        print("No se pudo completar la carga:", error)
    except (OSError, EOFError) as error:
        print("No se pudo leer el archivo de ubicaciones:", error)