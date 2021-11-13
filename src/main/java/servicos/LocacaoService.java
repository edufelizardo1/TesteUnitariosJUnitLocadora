package servicos;

import static utils.DataUtils.adicionarDias;

import java.nio.file.FileAlreadyExistsException;
import java.util.Date;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import utils.DataUtils;


public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmesSemEstoqueException, LocadoraException {
		if (usuario == null) {
			throw new LocadoraException("Usuário vazio.");
		}
		if (filme == null) {
			throw new LocadoraException("Filme vazio.");
		}
		if (filme.getEstoque() == 0) {
			throw new FilmesSemEstoqueException();
		}
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	/**
	 * @param args teste do método
	 */
	public static void main(String[] args) {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		//acao
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(usuario, filme);
			//verificacao
			System.out.println(locacao.getValor() == 5.0);
			System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
			System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}