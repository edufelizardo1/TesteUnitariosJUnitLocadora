package servicos;

import builders.FilmeBuilder;
import builders.UsuarioBuilder;
import daos.LocacaoDAO;
import daos.LocacaoDAOFake;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static builders.FilmeBuilder.umFilme;
import static builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Data Driver Test
 */
@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoDAO dao;
    private SPCService spc;

    private LocacaoService service;
    @Parameter
    public List<Filme> filmes;
    @Parameter(value = 1)
    public Double valorLocacao;
    @Parameter(value = 2)
    public String cenario;

    @Before
    public  void setup () {
        service = new LocacaoService();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        spc = Mockito.mock(SPCService.class);
        service.setSPCService(spc);
    }

    private static final Filme filme1 = umFilme().agora();
    private static final Filme filme2 = umFilme().agora();
    private static final Filme filme3 = umFilme().agora();
    private static final Filme filme4 = umFilme().agora();
    private static final Filme filme5 = umFilme().agora();
    private static final Filme filme6 = umFilme().agora();
    private static final Filme filme7 = umFilme().agora();

    /**
     *
     * @return Collection
     */
    @Parameters(name = "Teste {index} = {2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0,  "6 Filmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0,  "7 Filmes: Sem desconto"},
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos () throws LocadoraException, FilmesSemEstoqueException {
//        cenario
        Usuario usuario = umUsuario().agora();

//        Ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

//        Verificação
        assertThat(locacao.getValor(), is(valorLocacao));
    }
}
