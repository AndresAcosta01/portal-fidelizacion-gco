package com.gco.fidelizacion.services.cliente;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.dtos.cliente.ClienteRequestDTO;
import com.gco.fidelizacion.dtos.cliente.ClienteResponseDTO;
import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Cliente;
import com.gco.fidelizacion.models.Marca;
import com.gco.fidelizacion.models.TipoIdentificacion;
import com.gco.fidelizacion.repositories.ICiudadRepositorio;
import com.gco.fidelizacion.repositories.IClienteRepositorio;
import com.gco.fidelizacion.repositories.IMarcaRepositorio;
import com.gco.fidelizacion.repositories.ITipoIdentificacionRepositorio;
import com.gco.fidelizacion.validations.cliente.IValidacionCliente;

@Service
public class ServicioClienteImpl implements IServicioCliente {

        private final IClienteRepositorio repositorioCliente;
        private final ITipoIdentificacionRepositorio repositorioTipoIdentificacion;
        private final ICiudadRepositorio repositorioCiudad;
        private final IMarcaRepositorio repositorioMarca;
        private final IValidacionCliente validacionCliente;

        public ServicioClienteImpl(IClienteRepositorio repositorioCliente,
                        ITipoIdentificacionRepositorio repositorioTipoIdentificacion,
                        ICiudadRepositorio repositorioCiudad,
                        IMarcaRepositorio repositorioMarca, IValidacionCliente validacionCliente) {
                this.repositorioCliente = repositorioCliente;
                this.repositorioTipoIdentificacion = repositorioTipoIdentificacion;
                this.repositorioCiudad = repositorioCiudad;
                this.repositorioMarca = repositorioMarca;
                this.validacionCliente = validacionCliente;
        }

        @Override
        public ClienteResponseDTO crear(ClienteRequestDTO dto) {

                Cliente datosCliente = dto.toEntity();

                TipoIdentificacion tipoIdentificacion = repositorioTipoIdentificacion
                                .findById(dto.idTipoIdentificacion())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el tipo de identificación"));

                Ciudad ciudad = repositorioCiudad
                                .findById(dto.idCiudad())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró la ciudad"));

                Marca marca = repositorioMarca
                                .findById(dto.idMarca())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró la marca"));

                datosCliente.setTipoIdentificacion(tipoIdentificacion);
                datosCliente.setCiudad(ciudad);
                datosCliente.setMarca(marca);

                validacionCliente.validarCliente(datosCliente);

                return ClienteResponseDTO.fromEntity(repositorioCliente.save(datosCliente));
        }

        @Override
        public List<ClienteResponseDTO> listar() {

                return repositorioCliente.findAll()
                                .stream()
                                .map(ClienteResponseDTO::fromEntity)
                                .toList();
        }

        @Override
        public ClienteResponseDTO buscarPorId(UUID id) {

                Cliente cliente = repositorioCliente.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el cliente"));

                return ClienteResponseDTO.fromEntity(cliente);
        }

        @Override
        public ClienteResponseDTO buscarPorDocumento(UUID idTipoIdentificacion, String numeroIdentificacion) {

                Cliente cliente = repositorioCliente
                                .findByTipoIdentificacion_IdAndNumeroIdentificacion(idTipoIdentificacion,
                                                numeroIdentificacion)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el cliente"));

                return ClienteResponseDTO.fromEntity(cliente);
        }

        @Override
        public ClienteResponseDTO actualizar(UUID id, ClienteRequestDTO dto) {

                Cliente cliente = repositorioCliente.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el cliente"));

                TipoIdentificacion tipoIdentificacion = repositorioTipoIdentificacion
                                .findById(dto.idTipoIdentificacion())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el tipo de identificación"));

                Ciudad ciudad = repositorioCiudad
                                .findById(dto.idCiudad())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró la ciudad"));

                Marca marca = repositorioMarca
                                .findById(dto.idMarca())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró la marca"));

                cliente.setNumeroIdentificacion(dto.numeroIdentificacion());
                cliente.setNombres(dto.nombres());
                cliente.setApellidos(dto.apellidos());
                cliente.setFechaNacimiento(dto.fechaNacimiento());
                cliente.setDireccion(dto.direccion());
                cliente.setTipoIdentificacion(tipoIdentificacion);
                cliente.setCiudad(ciudad);
                cliente.setMarca(marca);

                validacionCliente.validarClienteActualizacion(id, cliente);

                return ClienteResponseDTO.fromEntity(repositorioCliente.save(cliente));
        }

        @Override
        public void eliminar(UUID id) {

                Cliente cliente = repositorioCliente.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "No se encontró el cliente"));

                repositorioCliente.delete(cliente);
        }

}
