package br.com.fiap.clientes.service;

import br.com.fiap.clientes.model.Cliente;
import br.com.fiap.clientes.repository.ClienteRepository;
import br.com.fiap.clientes.service.ClienteNotFoundException;
import br.com.fiap.clientes.service.ClienteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Page<Cliente> page = null;

    @Before
    public void setup() {
        Cliente cli1 = new Cliente(1L, "cliente1", "1234567890", 25);
        Cliente cli2 = new Cliente(2L, "cliente2", "0987654321", 32);

        page = new PageImpl<Cliente>(Arrays.asList(cli1, cli2));
    }

    @Test
    public void shoudRetrieveAllClientes() {
        when(clienteRepository.findAll(any(Pageable.class))).thenReturn(page);
        assertThat(clienteService.findAll(PageRequest.of(0, 10))).isEqualTo(page);
        verify(clienteRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void shouldFindClienteById() {
        Cliente cli1 = new Cliente(1L, "cliente1", "1234567890", 25);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cli1));
        assertThat(clienteService.findById(1L)).isEqualTo(cli1);
        verify(clienteRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldThrowClienteNotFoundExceptionForClienteByIdNull() {
        assertThatExceptionOfType(ClienteNotFoundException.class)
                .isThrownBy(() -> clienteService.findById(null)).withMessage("id invalido");
        verify(clienteRepository, times(0)).findById(null);
    }

    @Test
    public void shouldThrowClienteNotFoundForClienteWithoutValidId() {
        when(clienteRepository.findById(3L)).thenThrow(ClienteNotFoundException.class);
        assertThatExceptionOfType(ClienteNotFoundException.class)
                .isThrownBy(() -> clienteService.findById(3L));
        verify(clienteRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldAddAValidCliente() {
        Cliente cli1 = new Cliente(null, "cliente1", "1234567890", 25);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {cli1.setId(1L); return cli1;});

        Cliente res = clienteService.addCliente(cli1);
        //res.setId(1L);
        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getId()).isEqualTo(cli1.getId());
    }
}