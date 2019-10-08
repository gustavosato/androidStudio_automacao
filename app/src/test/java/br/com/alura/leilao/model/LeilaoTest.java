package br.com.alura.leilao.model;

import android.app.Notification;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.exception.LanceMenorQueUltimoLanceException;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioJaDeuCincoLancesException;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LeilaoTest {

    public static final double DELTA = 0.00001;
    public static final String ALEX = "Alex";
    private final Leilao CONSOLE = new Leilao("Console");
    private final Usuario GUSTAVO = new Usuario("Gustavo");

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void deve_DevolverDescricao_QuandoRecebeDescricao() {
        //Criar cenario de teste

        //Executar ação esperada
        String descricaoDevolvida = CONSOLE.getDescricao();

        //Testar resultado esperado
        assertThat(descricaoDevolvida, is(equalTo("Console")));
    }


    //[nome do método] [estado de teste] [resultado esperado]
    //[deve][resultado esperado][estado de teste]
    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeApenasUmLance() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));

        double maiorLanceDevolvido = CONSOLE.getMaiorLance();

        //assertEquals(200.00, maiorLanceDevolvido, DELTA);
        assertThat(maiorLanceDevolvido, closeTo(200.00,DELTA));
    }

    @Test
    public void deve_DevolverMaiorLance_QuandoRecebeMaisDeUmLanceEmOrderCrescente() {
        CONSOLE.propoe(new Lance(GUSTAVO, 100.00));
        CONSOLE.propoe(new Lance(new Usuario("Alex"), 200.00));

        double maiorLanceDevolvido = CONSOLE.getMaiorLance();

        assertEquals(200.00, maiorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverMenorLAnce_QuandoRecebeApenasUmLance() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));

        double menorLanceDevolvido = CONSOLE.getMenorLance();

        assertEquals(200.00, menorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverMenorLance_QuandoRecebeMaisDeUmLanceEmOrderCrescente() {
        CONSOLE.propoe(new Lance(GUSTAVO, 100.00));
        CONSOLE.propoe(new Lance(new Usuario("Alex"), 200.00));

        double menorLanceDevolvido = CONSOLE.getMenorLance();

        assertEquals(100.00, menorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeExatosTresLances() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));
        CONSOLE.propoe(new Lance(new Usuario("Alex"), 300.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 400.00));

        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();

        //assertEquals(3, tresMaioresLancesDevolvidos.size());
        assertThat(tresMaioresLancesDevolvidos, hasSize(equalTo(3)));
        //assertEquals(400.00, tresMaioresLancesDevolvidos.get(0).getValor(), DELTA);
        assertThat(tresMaioresLancesDevolvidos, hasItem(new Lance(GUSTAVO, 400.00)));
        //assertEquals(300.00, tresMaioresLancesDevolvidos.get(1).getValor(), DELTA);
        //assertEquals(200.00, tresMaioresLancesDevolvidos.get(2).getValor(), DELTA);
        /*assertThat(tresMaioresLancesDevolvidos, contains(
                new Lance(GUSTAVO, 400.00),
                new Lance(new Usuario("Alex"), 300.00),
                new Lance(GUSTAVO, 200.00)));*/

        assertThat(tresMaioresLancesDevolvidos, both(Matchers.<Lance>hasSize(3))
                .and(contains(
                        new Lance(GUSTAVO, 400.00),
                        new Lance(new Usuario("Alex"), 300.00),
                        new Lance(GUSTAVO, 200.00))));
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoNaoRecebeLances() {
        List<Lance> tresMaioresLAncesDevolvidos = CONSOLE.tresMaioresLances();

        assertEquals(0, tresMaioresLAncesDevolvidos.size());
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeApenasUmLances() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));
        List<Lance> tresMaioresLAncesDevolvidos = CONSOLE.tresMaioresLances();

        assertEquals(1, tresMaioresLAncesDevolvidos.size());
        assertEquals(200.00, tresMaioresLAncesDevolvidos.get(0).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeApenasDoisLances() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));
        CONSOLE.propoe(new Lance(new Usuario("Alex"), 300.00));

        List<Lance> tresMaioresLancesDevolvidos = CONSOLE.tresMaioresLances();

        assertEquals(2, tresMaioresLancesDevolvidos.size());
        assertEquals(300.00, tresMaioresLancesDevolvidos.get(0).getValor(), DELTA);
        assertEquals(200.00, tresMaioresLancesDevolvidos.get(1).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverTresMaioresLances_QuandoRecebeMaisDeTresLances() {
        CONSOLE.propoe(new Lance(GUSTAVO, 200.00));
        final Usuario ALEX = new Usuario("Alex");
        CONSOLE.propoe(new Lance(ALEX, 300.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 400.00));
        CONSOLE.propoe(new Lance(ALEX, 500.00));

        final List<Lance> tresMaioresLancesDevolvidosParaQuatroLances = CONSOLE.tresMaioresLances();

        assertEquals(3, tresMaioresLancesDevolvidosParaQuatroLances.size());
        assertEquals(500.00, tresMaioresLancesDevolvidosParaQuatroLances.get(0).getValor(), DELTA);
        assertEquals(400.00, tresMaioresLancesDevolvidosParaQuatroLances.get(1).getValor(), DELTA);
        assertEquals(300.00, tresMaioresLancesDevolvidosParaQuatroLances.get(2).getValor(), DELTA);


        final List<Lance> tresMaioresLancesParaCincoLances = CONSOLE.tresMaioresLances();

        assertEquals(3, tresMaioresLancesParaCincoLances.size());
        assertEquals(500.00, tresMaioresLancesParaCincoLances.get(0).getValor(), DELTA);
        assertEquals(400.00, tresMaioresLancesParaCincoLances.get(1).getValor(), DELTA);
        assertEquals(300.00, tresMaioresLancesParaCincoLances.get(2).getValor(), DELTA);
    }

    @Test
    public void deve_DevolverValorZeroParaMaiorLance_QuandoNaoTiverLance() {

        double maiorLanceDevolvido = CONSOLE.getMaiorLance();

        assertEquals(0.0, maiorLanceDevolvido, DELTA);
    }

    @Test
    public void deve_DevolverValorZeroParaMenorLance_QuandoNaoTiverLance() {

        double menorLanceDevolvido = CONSOLE.getMenorLance();

        assertEquals(0.0, menorLanceDevolvido, DELTA);
    }

    @Test(expected = LanceMenorQueUltimoLanceException.class)
    public void naoDeve_AdicionarLance_QuandoForMenorQueOMaiorLance() {
        CONSOLE.propoe(new Lance(GUSTAVO, 500.00));
        CONSOLE.propoe(new Lance(new Usuario("Fran"), 400.00));
    }

    @Test(expected = LanceSeguidoDoMesmoUsuarioException.class)
    public void naoDeve_AdicionarLance_QuandoForForOMesmoUsuarioDoUltimoLance() {
        CONSOLE.propoe(new Lance(GUSTAVO, 500.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 600.00));
    }

    @Test(expected = UsuarioJaDeuCincoLancesException.class)
    public void naoDeve_AdicionarLance_QuandoUsuarioDerCincoLance() {
        CONSOLE.propoe(new Lance(GUSTAVO, 100.00));
        final Usuario FRAN = new Usuario("Fran");
        CONSOLE.propoe(new Lance(FRAN, 200.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 300.00));
        CONSOLE.propoe(new Lance(FRAN, 400.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 500.00));
        CONSOLE.propoe(new Lance(FRAN, 600.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 700.00));
        CONSOLE.propoe(new Lance(FRAN, 800.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 900.00));
        CONSOLE.propoe(new Lance(FRAN, 1000.00));
        CONSOLE.propoe(new Lance(GUSTAVO, 1100.00));
    }

    /*@Test
    public void naoDeve_AdicionarLance_QuandoUsuarioDerCincoLances() {
        final Usuario FRAN = new Usuario("Fran");

        final Leilao console = new LeilaoBuilder("Console")
                .lance(GUSTAVO, 100.0)
                .lance(FRAN, 200.0)
                .lance(GUSTAVO, 300.0)
                .lance(FRAN, 400.0)
                .lance(GUSTAVO, 500.0)
                .lance(FRAN, 600.0)
                .lance(GUSTAVO, 700.0)
                .lance(FRAN, 800.0)
                .lance(GUSTAVO, 900.0)
                .lance(FRAN, 1000.0)
                .lance(GUSTAVO, 1100.0)
                .build();

        int quantidadeLancesDevolvida = console.quantidadeLances();

        assertEquals(10, quantidadeLancesDevolvida);
    }*/
}