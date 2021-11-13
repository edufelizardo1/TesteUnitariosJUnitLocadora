package testes;

import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import servicos.LocacaoService;
import utils.DataUtils;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static utils.DataUtils.*;

public class Testes {

    @Test
    public void Teste () {
        assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1, 1);
        Assert.assertEquals(0.512, 0.51, 0.01);
        Assert.assertEquals(Math.PI, 3.14, 0.01);

        int i = 5;
        Integer o = 5;
        Assert.assertEquals(Integer.valueOf(i), o);
        Assert.assertEquals(i, o.intValue());

         /**
         * brincadeira de criança
         */

         Integer nu1 = 10;

         Assert.assertEquals(20, nu1 + nu1);
         Assert.assertNotEquals(10, nu1 + nu1);

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = u2;
        /**
         * Objeto Equals referenciado na classe usuário
         */
        Assert.assertEquals(u1, u2);

        Assert.assertNotSame(u1, u2);
        Assert.assertSame(u3, u2);

        /**
         * Comparação
         */

        Assert.assertEquals("Erro de comparação", 1, 1);

    }
}
