package servicos;

import buildermaster.BuilderMaster;
import daos.LocacaoDAO;
import daos.LocacaoDAOFake;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import utils.DataUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static builders.FilmeBuilder.umFilme;
import static builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static utils.DataUtils.isMesmaData;
import static utils.DataUtils.obterDataComDiferencaDias;

public class LocacaoServiceTest {

    private LocacaoService service;
    private SPCService spc;
    private LocacaoDAO dao;
    private static int contador = 0;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public  void setup () {
        service = new LocacaoService();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        spc = Mockito.mock(SPCService.class);
        service.setSPCService(spc);
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
//        Arrange
        Usuario usuario = umUsuario().agora();
        List<Filme> filme = Arrays.asList(umFilme().comValor().agora());

//        Action
        Locacao locacao = service.alugarFilme(usuario, filme);

//        Assert

        errorCollector.checkThat( locacao.getValor(), is(equalTo(5.0)));
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
        Usuario usuario = umUsuario().agora();
        List<Filme> filme = Arrays.asList(umFilme().semEstoque().agora());

//        Action
        service.alugarFilme(usuario, filme);
    }
/****************************************************************************************/
                        /**** Descreve as formas de criar um teste ****/
    /**
     * Robusta
     */

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
        List<Filme> filme = Arrays.asList(umFilme().agora());
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
        Usuario usuario = umUsuario().agora();

        //Action
        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");
        service.alugarFilme(usuario, null);
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado () throws LocadoraException, FilmesSemEstoqueException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
//        Cenário
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC () throws LocadoraException, FilmesSemEstoqueException {
//        Cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Usuário negativado.");
//        acao
        service.alugarFilme(usuario, filmes);
    }
}