package br.com.fiap.servicos.service;

import br.com.fiap.servicos.TestUtil;
import br.com.fiap.servicos.model.Chamado;
import br.com.fiap.servicos.repository.ChamadosRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ChamadosServiceIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ChamadosRepository repo;

    @Autowired
    private ChamadosService chamadosService;

    Chamado tkt1;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
        tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");
        tkt1.setDataAbertura(LocalDateTime.now().minusDays(1));
        tkt1.setDataFechamento(LocalDateTime.now());

        tkt1 = repo.save(tkt1);
    }

    @After
    public void clear() throws SQLException {
        repo.deleteAll();
        TestUtil.cleanupDatabase(dataSource);
    }

    @Test
    public void shoudRetrieveChamadoById() {
        final Chamado find = chamadosService.findById(tkt1.getId().toString());
        assertThat(tkt1).isEqualTo(find);
    }

    @Test
    public void shouldThrowChamadoNotFoundForTicketWithValidId() {
        String uuid = UUID.randomUUID().toString();
        assertThatThrownBy(() -> chamadosService.findById(uuid))
                .isInstanceOf(ChamadoNotFoundException.class)
                .hasMessage("chamado invalido: " + uuid);
    }

    @Test
    public void shouldAddAValidTicket() {
        Chamado tkt1 = new Chamado( 1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

        Chamado res = chamadosService.abrirChamado(tkt1);

        assertThat(res.getDataAbertura()).isBefore(LocalDateTime.now());
        assertThat(res.getDataFechamento()).isNull();
        assertThat(res.getId()).isEqualTo(tkt1.getId());
        assertThat(res.getCliente()).isEqualTo(1L);
    }


}
