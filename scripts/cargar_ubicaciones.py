import gzip
import ijson
import requests

URL_DATOS = "https://github.com/dr5hn/countries-states-cities-database/releases/download/v3.2-export.7/json-countries%2Bstates%2Bcities.json.gz"
URL_API_PAISES = "http://localhost:8080/api/paises"
URL_API_DEPARTAMENTOS = "http://localhost:8080/api/departamentos"
URL_API_CIUDADES = "http://localhost:8080/api/ciudades"
TIMEOUT = 30


def normalizar_nombre(nombre):
    return nombre.strip().casefold()


def indexar_por_nombre(registros):
    return {normalizar_nombre(registro["nombre"]): registro for registro in registros}


def obtener_lista(session, url, params=None):
    respuesta = session.get(url, params=params, timeout=TIMEOUT)
    respuesta.raise_for_status()
    return respuesta.json()


def obtener_o_crear(session, url, datos, registros_por_nombre):
    clave = normalizar_nombre(datos["nombre"])
    registro_encontrado = registros_por_nombre.get(clave)
    if registro_encontrado:
        return registro_encontrado, False
    respuesta = session.post(url, json=datos, timeout=TIMEOUT)
    respuesta.raise_for_status()
    registro_creado = respuesta.json()
    registros_por_nombre[normalizar_nombre(registro_creado["nombre"])] = registro_creado
    return registro_creado, True


def cargar_ubicaciones():
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
        session.headers.update({"Accept": "application/json"})
        paises_guardados = obtener_lista(session, URL_API_PAISES)
        paises_por_nombre = indexar_por_nombre(paises_guardados)
        with session.get(URL_DATOS, timeout=60, stream=True) as respuesta:
            respuesta.raise_for_status()
            with gzip.GzipFile(fileobj=respuesta.raw) as archivo_gzip:
                paises = ijson.items(archivo_gzip, "item")
                for pais in paises:
                    nombre_pais = pais["name"]
                    departamentos = pais.get("states") or []
                    datos_pais = {
                        "nombre": nombre_pais,
                        "activo": True,
                    }
                    print("Procesando país:", nombre_pais)
                    print("Divisiones administrativas:", len(departamentos))
                    try:
                        pais_guardado, pais_creado = obtener_o_crear(
                            session,
                            URL_API_PAISES,
                            datos_pais,
                            paises_por_nombre,
                        )
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
                        departamentos_guardados = obtener_lista(
                            session,
                            URL_API_DEPARTAMENTOS,
                            params={"idPais": id_pais},
                        )
                    except requests.RequestException as error:
                        totales["errores"] += 1
                        print(
                            "Error consultando departamentos de:",
                            nombre_pais,
                            "-",
                            error,
                        )
                        continue
                    departamentos_por_nombre = indexar_por_nombre(
                        departamentos_guardados
                    )
                    departamentos_creados_pais = 0
                    departamentos_existentes_pais = 0
                    ciudades_creadas_pais = 0
                    ciudades_existentes_pais = 0
                    for departamento in departamentos:
                        nombre_departamento = departamento["name"]
                        ciudades = departamento.get("cities") or []
                        datos_departamento = {
                            "nombre": nombre_departamento,
                            "activo": True,
                            "idPais": id_pais,
                        }
                        try:
                            departamento_guardado, departamento_creado = (
                                obtener_o_crear(
                                    session,
                                    URL_API_DEPARTAMENTOS,
                                    datos_departamento,
                                    departamentos_por_nombre,
                                )
                            )
                        except requests.RequestException as error:
                            totales["errores"] += 1
                            print(
                                "Error procesando división:",
                                nombre_departamento,
                                "-",
                                error,
                            )
                            continue
                        id_departamento = departamento_guardado["id"]
                        if departamento_creado:
                            totales["departamentos_creados"] += 1
                            departamentos_creados_pais += 1
                        else:
                            totales["departamentos_existentes"] += 1
                            departamentos_existentes_pais += 1
                        try:
                            ciudades_guardadas = obtener_lista(
                                session,
                                URL_API_CIUDADES,
                                params={"idDepartamento": id_departamento},
                            )
                        except requests.RequestException as error:
                            totales["errores"] += 1
                            print(
                                "Error consultando ciudades de:",
                                nombre_departamento,
                                "-",
                                error,
                            )
                            continue
                        ciudades_por_nombre = indexar_por_nombre(ciudades_guardadas)
                        ciudades_creadas_departamento = 0
                        ciudades_existentes_departamento = 0
                        for ciudad in ciudades:
                            datos_ciudad = {
                                "nombre": ciudad["name"],
                                "activo": True,
                                "idDepartamento": id_departamento,
                            }
                            try:
                                _, ciudad_creada = obtener_o_crear(
                                    session,
                                    URL_API_CIUDADES,
                                    datos_ciudad,
                                    ciudades_por_nombre,
                                )
                            except requests.RequestException as error:
                                totales["errores"] += 1
                                print(
                                    "Error procesando ciudad:",
                                    ciudad["name"],
                                    "-",
                                    error,
                                )
                                continue
                            if ciudad_creada:
                                totales["ciudades_creadas"] += 1
                                ciudades_creadas_pais += 1
                                ciudades_creadas_departamento += 1
                            else:
                                totales["ciudades_existentes"] += 1
                                ciudades_existentes_pais += 1
                                ciudades_existentes_departamento += 1
                        print(
                            nombre_departamento,
                            "- ciudades nuevas:",
                            ciudades_creadas_departamento,
                            "- existentes:",
                            ciudades_existentes_departamento,
                        )
                    print("Resumen país:", nombre_pais)
                    print("Departamentos nuevos:", departamentos_creados_pais)
                    print("Departamentos existentes:", departamentos_existentes_pais)
                    print("Ciudades nuevas:", ciudades_creadas_pais)
                    print("Ciudades existentes:", ciudades_existentes_pais)
    print("Carga finalizada")
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
