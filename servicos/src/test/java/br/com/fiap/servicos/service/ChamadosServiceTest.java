package br.com.fiap.servicos.service;

import br.com.fiap.servicos.model.Chamado;
import br.com.fiap.servicos.repository.ChamadosRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ChamadosServiceTest {

    @Mock
    private ChamadosRepository chamadosRepository;

    @InjectMocks
    private ChamadosService chamadosService;

    @Test
    public void shouldFindChamadoById() {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        String uuid = UUID.randomUUID().toString();

        when(chamadosRepository.findById(any(UUID.class)))
                .thenAnswer(i -> {
                    tkt1.setId(UUID.fromString(uuid));
                    tkt1.setDataAbertura(LocalDateTime.now());
                    tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                            "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                            " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                            "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                            "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

                    return Optional.of(tkt1);
                });
        Chamado res = chamadosService.findById(uuid);
        assertThat(res.getId()).isNotNull();
        assertThat(res.getId().toString()).isEqualTo(uuid);
        assertThat(res.getDataAbertura()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(res.getCliente()).isEqualTo(tkt1.getCliente());
        assertThat(res.getMotivo()).isEqualTo(tkt1.getMotivo());
        assertThat(res.getDescricao()).isNotEmpty();
        verify(chamadosRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void shouldThrowChamadoNotFoundExceptionForTicketByIdInvalid() {
        assertThatExceptionOfType(ChamadoNotFoundException.class)
                .isThrownBy(() -> chamadosService.findById("aquiumidinvalido")).withMessage("chamado invalido");
        verify(chamadosRepository, times(0)).findById(any(UUID.class));
    }

    @Test
    public void shouldThrowChamadoNotFoundForTiketWithoutValidId() {
        when(chamadosRepository.findById(UUID.randomUUID())).thenThrow(ChamadoNotFoundException.class);
        assertThatExceptionOfType(ChamadoNotFoundException.class)
                .isThrownBy(() -> chamadosService.findById(UUID.randomUUID().toString()));
        verify(chamadosRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void shouldOpenTicket() {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                        "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                        " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                        "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                        "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

        when(chamadosRepository.save(any(Chamado.class)))
                .thenAnswer(i -> {
                    tkt1.setId(UUID.randomUUID());
                    return tkt1;
                });

        Chamado res = chamadosService.abrirChamado(tkt1);
        assertThat(res.getDataAbertura()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(res.getDataFechamento()).isNull();
        assertThat(res.getId()).isEqualTo(tkt1.getId());
        assertThat(res.getCliente()).isEqualTo(1L);
    }

    @Test
    public void shouldCloseTicket() {
        Chamado tkt1 = new Chamado(1L, "filme nao funciona");
        tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

        String uuid = UUID.randomUUID().toString();
        when(chamadosRepository.findById(any(UUID.class)))
                .thenAnswer(i -> {
                    tkt1.setId(UUID.fromString(uuid));
                    tkt1.setDataAbertura(LocalDateTime.now());
                    tkt1.setDescricao("Contentam solicitar meu envenenou dementava nas lar rapaselho belchiore. Ordenou quantas teu tua " +
                            "fizeram ficamos das boa simples. Ca de submetter repetidas eu comparsas. Firmeza dez mas mal" +
                            " diz degraus queriam. Ha operem justas na chamma. Dou lhe litigantes sympathias gravemente ira. " +
                            "Busquemos essencial desalinho so desataram as na respondeu encontrou. Minima abysmo animar ar " +
                            "sentar forcar tornas os da. Encostado emmudecer rua clamoroso dei foi viu contribue.");

                    return Optional.of(tkt1);
                });
        when(chamadosRepository.save(any(Chamado.class)))
                .thenAnswer(i -> {
                    tkt1.setId(UUID.fromString(uuid));
                    tkt1.setDataFechamento(LocalDateTime.now());
                    return tkt1;
                });

        Chamado res = chamadosService.fecharChamado(uuid);
        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getId()).isEqualTo(tkt1.getId());
        assertThat(res.getDataFechamento()).isNotNull();
        assertThat(res.getDataFechamento()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(res.getCliente()).isEqualTo(1L);
    }
}