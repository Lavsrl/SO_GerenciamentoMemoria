import java.util.*;

public class GerenciadorMemoria {
    private int[] memoria;
    private int ultimaPosicao = 0;
    private Map<Integer, List<Integer>> quickFitMap;

    public GerenciadorMemoria(int tamanho) {
        this.memoria = new int[tamanho];
        Arrays.fill(memoria, 0);
        quickFitMap = new HashMap<>();
    }

    private int firstFit(int tamanho) {
        int blocosLivres = 0;
        for (int i = 0; i < memoria.length; i++) {
            if (memoria[i] == 0) {
                blocosLivres++;
                if (blocosLivres == tamanho) {
                    return i - tamanho + 1;
                }
            } else {
                blocosLivres = 0;
            }
        }
        return -1;
    }

    private int nextFit(int tamanho) {
        int blocosLivres = 0;
        for (int i = ultimaPosicao; i < memoria.length + ultimaPosicao; i++) {
            int posicao = i % memoria.length;
            if (memoria[posicao] == 0) {
                blocosLivres++;
                if (blocosLivres == tamanho) {
                    ultimaPosicao = (posicao + 1) % memoria.length;
                    return posicao - tamanho + 1;
                }
            } else {
                blocosLivres = 0;
            }
        }
        return -1;
    }

    private int bestFit(int tamanho) {
        int melhorInicio = -1;
        int menorEspaco = Integer.MAX_VALUE;

        int blocosLivres = 0;
        int inicioAtual = -1;

        for (int i = 0; i < memoria.length; i++) {
            if (memoria[i] == 0) {
                if (blocosLivres == 0) {
                    inicioAtual = i;
                }
                blocosLivres++;
            } else {
                if (blocosLivres >= tamanho && blocosLivres < menorEspaco) {
                    menorEspaco = blocosLivres;
                    melhorInicio = inicioAtual;
                }
                blocosLivres = 0;
            }
        }
        if (blocosLivres >= tamanho && blocosLivres < menorEspaco) {
            melhorInicio = inicioAtual;
        }

        return melhorInicio;
    }

    private int quickFit(int tamanho) {
        if (quickFitMap.containsKey(tamanho) && !quickFitMap.get(tamanho).isEmpty()) {
            return quickFitMap.get(tamanho).remove(0);
        }
        return firstFit(tamanho);
    }

    private int worstFit(int tamanho) {
        int piorInicio = -1;
        int maiorEspaco = -1;

        int blocosLivres = 0;
        int inicioAtual = -1;

        for (int i = 0; i < memoria.length; i++) {
            if (memoria[i] == 0) {
                if (blocosLivres == 0) {
                    inicioAtual = i;
                }
                blocosLivres++;
            } else {
                if (blocosLivres >= tamanho && blocosLivres > maiorEspaco) {
                    maiorEspaco = blocosLivres;
                    piorInicio = inicioAtual;
                }
                blocosLivres = 0;
            }
        }

        if (blocosLivres >= tamanho && blocosLivres > maiorEspaco) {
            piorInicio = inicioAtual;
        }

        return piorInicio;
    }

    public void desalocar(int processo) {
        for (int i = 0; i < memoria.length; i++) {
            if (memoria[i] == processo) {
                memoria[i] = 0; // Marca como livre
            }
        }
    }

    public boolean alocar(String processoId, int tamanho, String estrategia) {
        int inicio = -1;

        switch (estrategia.toLowerCase()) {
            case "first":
                inicio = firstFit(tamanho);
                break;
            case "next":
                inicio = nextFit(tamanho);
                break;
            case "best":
                inicio = bestFit(tamanho);
                break;
            case "quick":
                inicio = quickFit(tamanho);
                break;
            case "worst":
                inicio = worstFit(tamanho);
                break;
            default:
                throw new IllegalArgumentException("Estratégia inválida.");
        }

        if (inicio != -1) {
            for (int i = inicio; i < inicio + tamanho; i++) {
                memoria[i] = processoId.hashCode(); // Marca os blocos como alocados para o processo
            }
            System.out.println("Processo " + processoId + " alocado no bloco " + inicio);
            return true; // Alocação bem-sucedida
        } else {
            System.out.println("Não foi possível alocar o processo " + processoId + " (" + estrategia + ").");
            return false; // Falha na alocação
        }
    }

    public boolean isProcessoAlocado(String processoId) {
        for (int bloco : memoria) {
            if (bloco == processoId.hashCode()) {
            }
        }
        return false;
    }

    public void desalocarProcesso(String processoId) {
        desalocar(processoId.hashCode());
    }

    public void resetarMemoria() {
        Arrays.fill(memoria, 0);
        ultimaPosicao = 0;
    }

    public void imprimirMapaMemoria() {
        System.out.println(Arrays.toString(memoria));
    }

    public void exibirMemoria() {
        System.out.println(Arrays.toString(memoria));
    }
}