import java.util.*;

public class GerenciadorMemoria {
    private final int[] memoria;
    private final int tamanhoMemoria;
    private final List<Processo> processos;
    private int ponteiroNextFit = 0;
    private final Map<Integer, List<Integer>> quickFitLists = new HashMap<>();

    public GerenciadorMemoria(int tamanhoMemoria, List<Processo> processos) {
        this.tamanhoMemoria = tamanhoMemoria;
        this.memoria = new int[tamanhoMemoria];
        this.processos = processos;
        inicializarQuickFit();
    }

    public boolean FirstFit(Processo processo) {
        for (int i = 0; i <= tamanhoMemoria - processo.getTamanho(); i++) {
            if (isLivre(i, processo.getTamanho())) {
                alocar(processo, i);
                return true;
            }
        }
        return false;
    }

    public boolean NextFit(Processo processo) {
        int start = ponteiroNextFit;
        do {
            if (isLivre(ponteiroNextFit, processo.getTamanho())) {
                alocar(processo, ponteiroNextFit);
                return true;
            }
            ponteiroNextFit = (ponteiroNextFit + 1) % tamanhoMemoria;
        } while (ponteiroNextFit != start);
        return false;
    }

    public boolean BestFit(Processo processo) {
        int melhorIndice = -1;
        int menorTamanho = Integer.MAX_VALUE;

        for (int i = 0; i <= tamanhoMemoria - processo.getTamanho(); i++) {
            if (isLivre(i, processo.getTamanho())) {
                int tamanhoLivre = calcularBlocoLivre(i);
                if (tamanhoLivre < menorTamanho) {
                    melhorIndice = i;
                    menorTamanho = tamanhoLivre;
                }
            }
        }

        if (melhorIndice != -1) {
            alocar(processo, melhorIndice);
            return true;
        }
        return false;
    }

    public boolean QuickFit(Processo processo) {
        List<Integer> listaTamanho = quickFitLists.get(processo.getTamanho());
        if (listaTamanho != null && !listaTamanho.isEmpty()) {
            int indice = listaTamanho.remove(0);
            alocar(processo, indice);
            return true;
        }
        return FirstFit(processo);
    }

    public boolean WorstFit(Processo processo) {
        int piorIndice = -1;
        int maiorTamanho = -1;

        for (int i = 0; i <= tamanhoMemoria - processo.getTamanho(); i++) {
            if (isLivre(i, processo.getTamanho())) {
                int tamanhoLivre = calcularBlocoLivre(i);
                if (tamanhoLivre > maiorTamanho) {
                    piorIndice = i;
                    maiorTamanho = tamanhoLivre;
                }
            }
        }

        if (piorIndice != -1) {
            alocar(processo, piorIndice);
            return true;
        }
        return false;
    }



    public void desalocar(Processo processo) {
        for (int i = processo.getInicio(); i < processo.getInicio() + processo.getTamanho(); i++) {
            memoria[i] = 0;
        }
        atualizarQuickFit(processo.getInicio(), processo.getTamanho());
        System.out.println("Processo " + processo.getId() + " desalocado.");
        processo.desalocarProcesso();
    }

    public boolean isLivre(int inicio, int tamanho) {
        if (inicio + tamanho > tamanhoMemoria) {
            return false; // Evita acessar fora dos limites da memória
        }
        for (int i = inicio; i < inicio + tamanho; i++) {
            if (memoria[i] == 1) {
                return false;
            }
        }
        return true;
    }

    public int calcularBlocoLivre(int inicio) {
        int tamanho = 0;
        for (int i = inicio; i < tamanhoMemoria && memoria[i] == 0; i++) {
            tamanho++;
        }
        return tamanho;
    }

    public int calcularFragmentacaoExterna(int menorTamanhoProcesso) {
        int fragmentacao = 0;
        int tamanhoAtual = 0;

        for (int i = 0; i < tamanhoMemoria; i++) {
            if (memoria[i] == 0) {
                tamanhoAtual++;
            } else {
                if (tamanhoAtual > 0 && tamanhoAtual < menorTamanhoProcesso) {
                    fragmentacao += tamanhoAtual;
                }
                tamanhoAtual = 0;
            }
        }

        if (tamanhoAtual > 0 && tamanhoAtual < menorTamanhoProcesso) {
            fragmentacao += tamanhoAtual;
        }

        return fragmentacao;
    }

    public void alocar(Processo processo, int inicio) {
        for (int i = inicio; i < inicio + processo.getTamanho(); i++) {
            memoria[i] = 1;
        }
        processo.alocarProcesso(inicio);
        System.out.println("Processo " + processo.getId() + " alocado em " + inicio);
    }

    public void exibirMemoria() {
        System.out.println(Arrays.toString(memoria));
    }

    public void inicializarQuickFit() {
        quickFitLists.clear();
        for (int i = 1; i <= tamanhoMemoria; i++) {
            quickFitLists.put(i, new ArrayList<>());
        }
    }

    public void atualizarQuickFit(int inicio, int tamanho) {
        quickFitLists.get(tamanho).add(inicio);
    }
}