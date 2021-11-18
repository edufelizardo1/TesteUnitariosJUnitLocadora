package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import servicos.CalculoValorLocacaoTest;
import servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocacaoServiceTest.class,
        CalculoValorLocacaoTest.class
})
public class SuitesExecucao {
}