package servicos;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
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
    public void deveAlugarFilme() throws Exception {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filme = Arrays.asList(new Filme("Filme 1", 2, 5.0));

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
    public void naoDevealugarFilmeSemEstoqueTest () throws Exception {
//        Arrange
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filme = Arrays.asList(new Filme("Filme 1", 0, 5.0));

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
        List<Filme> filme = Arrays.asList(new Filme("Filme 1", 0, 5.0));

//        Action
        try {
            service.alugarFilme(usuario, filme);
            fail("Deveria ter lançado uma exceção.");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque."));
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
        List<Filme> filme = Arrays.asList(new Filme("Filme 1", 0, 5.0));

//        Action
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Filme sem estoque.");
        service.alugarFilme(usuario, filme);

    }
/****************************************************************************************/

    @Test
    public void naoDeveAlugarFilmeSemUsuarioTest () throws FilmesSemEstoqueException {
        //Arrange
        List<Filme> filme = Arrays.asList(new Filme("Filme 1", 1, 5.0));
        Usuario usuario = null;

        //Action
        try {
            service.alugarFilme(usuario, filme);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio."));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilmeTest () throws LocadoraException, FilmesSemEstoqueException {
        //Arrange
        Usuario usuario = new Usuario("usuario 1");

        //Action
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);
    }

    /**
     * TDD
     */
    @Test
    public void devePagar75PctNoFilme3 () throws LocadoraException, FilmesSemEstoqueException {
//        cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0));

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        assertThat(locacao.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4 () throws LocadoraException, FilmesSemEstoqueException {
//        cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0));

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        assertThat(locacao.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme4 () throws LocadoraException, FilmesSemEstoqueException {
//        cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0));

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        assertThat(locacao.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme4 () throws LocadoraException, FilmesSemEstoqueException {
//        cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0),
                new Filme("Filme 6", 1, 4.0));

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        assertThat(locacao.getValor(), is(14.0));
    }
}