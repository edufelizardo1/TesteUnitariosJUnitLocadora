package servicos;

import static utils.DataUtils.adicionarDias;

import java.nio.file.FileAlreadyExistsException;
import java.util.Date;
import java.util.List;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exceptions.FilmesSemEstoqueException;
import exceptions.LocadoraException;
import utils.DataUtils;


public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmesSemEstoqueException, LocadoraException {
		if (usuario == null) {
			throw new LocadoraException("Usuário vazio.");
		}
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio.");
		}
		for (Filme filme: filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmesSemEstoqueException();
			}
		}
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
				case 2:
					valorFilme = valorFilme * 0.75;
					break;
				case 3:
					valorFilme = valorFilme * 0.5;
					break;
				case 4:
					valorFilme = valorFilme * 0.25;
					break;
				case 5:
					valorFilme = valorFilme * 0;
					break;
				default:
					break;
			}
			valorTotal += valorFilme;
		}
		locacao.setValor(valorTotal);

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
			locacao = service.alugarFilme(usuario, locacao.getFilmes());
			//verificacao
			System.out.println(locacao.getValor() == 5.0);
			System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
			System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}