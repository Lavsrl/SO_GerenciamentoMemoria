import java.util.*;

public class Main {
    public static void main(String[] args) {
        GerenciadorMemoria memoria = new GerenciadorMemoria(32);
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

        List<String> algoritmos = Arrays.asList("first", "next", "best", "quick", "worst");

        for (String algoritmo : algoritmos) {
            System.out.println("\n=== Executando com algoritmo: " + algoritmo.toUpperCase() + " ===");
            executarSorteio(memoria, processos, algoritmo);

            // Reinicia o estado da memória para o próximo algoritmo
            memoria.resetarMemoria();
        }
    }

    private static void executarSorteio(GerenciadorMemoria memoria, List<Processo> processos, String algoritmo) {
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            Processo processo = processos.get(random.nextInt(processos.size()));
            if (memoria.isProcessoAlocado(processo.getId())) {
                System.out.println("Sorteado: " + processo.getId() + " (Desalocando)");
                memoria.desalocarProcesso(processo.getId());
            } else {
                System.out.println("Sorteado: " + processo.getId() + " (Alocando " + processo.getTamanho() + " blocos)");
                boolean sucesso = memoria.alocar(processo.getId(), processo.getTamanho(), algoritmo);
                if (!sucesso) {
                    System.out.println("Falha ao alocar o processo " + processo.getId());
                }
            }

            memoria.imprimirMapaMemoria();
        }
    }
}