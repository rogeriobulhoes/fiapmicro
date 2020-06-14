package br.com.fiap.servicos.service;

import br.com.fiap.servicos.amqp.SendMessage;
import br.com.fiap.servicos.client.ClienteDiscoveryClient;
import br.com.fiap.servicos.client.FilmeDiscoveryClient;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Favorito;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.repository.FavoritosRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class FavoritosServiceTest {

    @InjectMocks
    FavoritosService favoritosService;

    @Mock
    FavoritosRepository favoritosRepository;

    @Mock
    FilmeDiscoveryClient filmeDiscoveryClient;

    @Mock
    ClienteDiscoveryClient clienteDiscoveryClient;

    @Mock
    SendMessage sendMessage;

    Favorito favorito;

    Filme fil1;
    Cliente cli1;

    @Before
    public void setup() {
        cli1 = new Cliente(1L, "cliente1", "1234567890", 25);
        fil1 = new Filme(2L, "Back To The Future", 1984, "ficção", "ingles", "filme", 0, 0);
        fil1.setDetalhe(
                "Old education him departure any arranging one prevailed. " +
                        "Their end whole might began her. Behaved the comfort another fifteen eat. " +
                        "Partiality had his themselves ask pianoforte increasing discovered. So mr delay " +
                        "at since place whole above miles. He to observe conduct at detract because. " +
                        "Way ham unwilling not breakfast furniture explained perpetual. " +
                        "Or mr surrounded conviction so astonished literature. Songs to an blush woman " +
                        "be sorry young. We certain as removal attempt.");

        favorito = new Favorito(cli1.getId(), fil1.getId());
    }

    @Test
    public void shouldMarkAFilmeAsFavorito() throws JsonProcessingException {
        when(clienteDiscoveryClient.findClienteById(anyLong())).thenReturn(cli1);
        when(filmeDiscoveryClient.findFilmeById(anyLong())).thenReturn(fil1);
        when(favoritosRepository.save(any(Favorito.class)))
                .thenAnswer(i -> {
                    favorito.setId(1L); return favorito;});
        when(favoritosRepository.existsByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(false);
        when(favoritosRepository.findByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(favorito);
        doNothing().when(favoritosRepository).deleteById(anyLong());
        doNothing().when(sendMessage).sendFavoritosMessage(anyLong(), anyInt());


        Favorito res = null;
        try {
            res = favoritosService.marcar(cli1.getId(), fil1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getClienteId()).isEqualTo(cli1.getId());
        assertThat(res.getFilmeId()).isEqualTo(fil1.getId());
    }


    @Test
    public void shouldRemoveAFavoritoMarkFromAFilme() throws JsonProcessingException {
        when(clienteDiscoveryClient.findClienteById(anyLong())).thenReturn(cli1);
        when(filmeDiscoveryClient.findFilmeById(anyLong())).thenReturn(fil1);
        when(favoritosRepository.save(any(Favorito.class)))
                .thenAnswer(i -> {
                    favorito.setId(1L); return favorito;});
        when(favoritosRepository.existsByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(true);
        when(favoritosRepository.findByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(favorito);
        doNothing().when(favoritosRepository).deleteById(anyLong());
        doNothing().when(sendMessage).sendFavoritosMessage(anyLong(), anyInt());


        Favorito res = null;
        try {
            res = favoritosService.marcar(cli1.getId(), fil1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(res.getClienteId()).isEqualTo(cli1.getId());
        assertThat(res.getFilmeId()).isEqualTo(fil1.getId());
    }

}
