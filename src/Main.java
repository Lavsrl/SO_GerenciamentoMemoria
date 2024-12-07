import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        int tamanhoMemoria = 32;

        List<Processo> processos = Arrays.asList(
                new Processo("P1", 5),
                new Processo("P2", 4),
                new Processo("P3", 2),
                new Processo("P4", 5),
                new Processo("P5", 8),
                new Processo("P6", 3),
                new Processo("P7", 5),
                new Processo("P8", 8),
                new Processo("P9", 2),
                new Processo("P10", 6)
        );

        GerenciadorMemoria memoria = new GerenciadorMemoria(tamanhoMemoria, processos);

        simular("First Fit", memoria::FirstFit, processos, memoria);
        simular("Next Fit", memoria::NextFit, processos, memoria);
        simular("Best Fit", memoria::BestFit, processos, memoria);
        simular("Quick Fit", memoria::QuickFit, processos, memoria);
        simular("Worst Fit", memoria::WorstFit, processos, memoria);
    }

    public static void simular(String nomeAlgoritmo, Function<Processo, Boolean> algoritmo, List<Processo> processos, GerenciadorMemoria gerenciador) {
        gerenciador.inicializarQuickFit();

        System.out.println("\n ** Algoritmo: " + nomeAlgoritmo + " **");
        int menorTamanhoProcesso = processos.stream().mapToInt(Processo::getTamanho).min().orElse(1);

        for (int i = 0; i < 30; i++) {
            Processo processo = processos.get(random.nextInt(processos.size()));
            if (processo.isAlocado()) {
                gerenciador.desalocar(processo);
            } else {
                if (!algoritmo.apply(processo)) {
                    System.out.println("Erro: Não foi possível alocar o " + processo.getId());
                }
            }
            gerenciador.exibirMemoria();
            System.out.println("Fragmentação externa: " + gerenciador.calcularFragmentacaoExterna(menorTamanhoProcesso));
        }
    }
}