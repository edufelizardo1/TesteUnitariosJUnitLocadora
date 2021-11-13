package servicos;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static utils.DataUtils.isMesmaData;
import static utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoServiceTest {

    private LocacaoService service;
    private static int contador = 0;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup () {
        service = new LocacaoService();
    }

    @Test
    public void alugarFilme() throws Exception {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        Filme filme = new Filme("filme 1", 2, 5.0);

//        Action
        Locacao locacao = service.alugarFilme(usuario, filme);

//        Assert
        errorCollector.checkThat( locacao.getValor(), is(5.0));
        errorCollector.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        errorCollector.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    /**
     * Elegante
     * @throws Exception
     */
    @Test(expected = FilmesSemEstoqueException.class)
    public void Locacao_FilmeSemEstoqueTest () throws Exception {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        Filme filme = new Filme("filme 1", 0, 5.0);

//        Action
        service.alugarFilme(usuario, filme);
    }
/****************************************************************************************/
                        /**** Descreve as formas de criar um teste ****/
    /**
     * Robusta
     */
    @Test
    @Ignore
    public void Locacao_FilmeSemEstoque2Test () {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        Filme filme = new Filme("filme 1", 0, 5.0);

//        Action
        try {
            service.alugarFilme(usuario, filme);
            Assert.fail("Deveria ter lançado uma exceção.");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque."));
        }

//        Assert
    }

    /**
     * Nova
     * @throws Exception
     */
    @Test
    @Ignore
    public void Locacao_FilmeSemEstoque3Test () throws Exception {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        Filme filme = new Filme("filme 1", 0, 5.0);

//        Action
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filme sem estoque.");
        service.alugarFilme(usuario, filme);

    }
/****************************************************************************************/

    @Test
    public void LocacaoUsuariovazioTest () throws FilmesSemEstoqueException {
        //Arrange
        Filme filme = new Filme("filme 1", 1, 5.0);
        Usuario usuario = null;

        //Action
        try {
            service.alugarFilme(usuario, filme);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário vazio."));
        }
    }

    @Test
    public void LocacaoFilmeVazioTest () throws LocadoraException, FilmesSemEstoqueException {
        //Arrange
        Usuario usuario = new Usuario("usuario 1");

        //Action
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);
    }
}