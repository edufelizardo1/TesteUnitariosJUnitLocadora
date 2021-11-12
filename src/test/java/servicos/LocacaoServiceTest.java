package servicos;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;
import utils.DataUtils;

import java.util.Date;

import static org.junit.Assert.*;

public class LocacaoServiceTest {

    @Test
    public void alugarFilme() {
//        Arrange
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("usuario 1");
        Filme filme = new Filme("filme 1", 2, 5.0);

//        Action
        Locacao locacao = service.alugarFilme(usuario, filme);

//        Assert
        Assert.assertTrue(locacao.getValor() == 5.0);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}