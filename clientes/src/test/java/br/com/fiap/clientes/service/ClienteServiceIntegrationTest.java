package br.com.fiap.clientes.service;

import br.com.fiap.clientes.TestUtil;
import br.com.fiap.clientes.model.Cliente;
import br.com.fiap.clientes.repository.ClienteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ClienteServiceIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private ClienteService clienteService;

    private Page<Cliente> page = null;

    List<Cliente> clientes = null;
    Cliente cli1, cli2;

    @Before
    public void setUp() throws Exception {

        cli1 = new Cliente(null, "cliente1", "1234567890", 25);
        cli2 = new Cliente(null, "cliente2", "0987654321", 32);

        clientes = Arrays.asList(cli1, cli2);

        repo.saveAll(clientes);

        System.out.println(">>>>> Ids cadastrados:" + cli1.getId() + "  " + cli2.getId());
    }

    @After
    public void clear() throws SQLException {
        repo.deleteAll();
        TestUtil.cleanupDatabase(dataSource);
    }

    @Test
    public void shoudRetrieveAllClientes() {
        page = new PageImpl<Cliente>(clientes);
        final Page<Cliente> all = clienteService.findAll(PageRequest.of(0, 10));
        assertThat(all.getContent()).isEqualTo(clientes);
    }

    @Test
    public void shoudRetrieveClienteById() {
        final Cliente find = clienteService.findById(2L);
        assertThat(clientes).contains(find);
        assertThat(find).isEqualTo(cli2);
    }

    @Test
    public void shouldThrowClienteNotFoundForClienteWithoutValidId() {
        assertThatThrownBy(() -> clienteService.findById(3L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessage("id: 3");
    }

    @Test
    public void shouldThrowClienteNotFoundExceptionForClienteByIdNull() {
        assertThatThrownBy(() -> clienteService.findById(null))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessage("id invalido");
    }

    @Test
    public void shouldAddAValidCliente() {
        Cliente cli1 = new Cliente(null, "cliente1", "1234567890", 25);

        Cliente res = clienteService.addCliente(cli1);

        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getId()).isEqualTo(cli1.getId());
    }


}
