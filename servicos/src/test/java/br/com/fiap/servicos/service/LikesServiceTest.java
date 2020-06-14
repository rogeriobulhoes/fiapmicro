package br.com.fiap.servicos.service;

import br.com.fiap.servicos.amqp.SendMessage;
import br.com.fiap.servicos.client.ClienteDiscoveryClient;
import br.com.fiap.servicos.client.FilmeDiscoveryClient;
import br.com.fiap.servicos.model.Cliente;
import br.com.fiap.servicos.model.Filme;
import br.com.fiap.servicos.model.Like;
import br.com.fiap.servicos.repository.LikesRepository;
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
public class LikesServiceTest {

    @InjectMocks
    LikesService likesService;

    @Mock
    LikesRepository likesRepository;

    @Mock
    FilmeDiscoveryClient filmeDiscoveryClient;

    @Mock
    ClienteDiscoveryClient clienteDiscoveryClient;

    @Mock
    SendMessage sendLikeMessage;

    Like like;

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

        like = new Like(cli1.getId(), fil1.getId());
    }

    @Test
    public void shouldGiveALikeToAMovie() throws JsonProcessingException {
        when(clienteDiscoveryClient.findClienteById(anyLong())).thenReturn(cli1);
        when(filmeDiscoveryClient.findFilmeById(anyLong())).thenReturn(fil1);
        when(likesRepository.save(any(Like.class)))
                .thenAnswer(i -> {
                    like.setId(1L); return like;});
        when(likesRepository.existsByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(false);
        when(likesRepository.findByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(like);
        doNothing().when(likesRepository).deleteById(anyLong());
        doNothing().when(sendLikeMessage).sendLikeMessage(anyLong(), anyInt());


        Like res = null;
        try {
            res = likesService.marcar(cli1.getId(), fil1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(res).hasNoNullFieldsOrProperties();
        assertThat(res.getClienteId()).isEqualTo(cli1.getId());
        assertThat(res.getFilmeId()).isEqualTo(fil1.getId());
    }


    @Test
    public void shouldRemoveALikeToAMovie() throws JsonProcessingException {
        when(clienteDiscoveryClient.findClienteById(anyLong())).thenReturn(cli1);
        when(filmeDiscoveryClient.findFilmeById(anyLong())).thenReturn(fil1);
        when(likesRepository.save(any(Like.class)))
                .thenAnswer(i -> {
                    like.setId(1L); return like;});
        when(likesRepository.existsByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(true);
        when(likesRepository.findByClienteIdAndFilmeId(anyLong(), anyLong())).thenReturn(like);
        doNothing().when(likesRepository).deleteById(anyLong());
        doNothing().when(sendLikeMessage).sendLikeMessage(anyLong(), anyInt());


        Like res = null;
        try {
            res = likesService.marcar(cli1.getId(), fil1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(res.getClienteId()).isEqualTo(cli1.getId());
        assertThat(res.getFilmeId()).isEqualTo(fil1.getId());
    }

}
