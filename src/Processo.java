public class Processo {
    private final String id;
    private final int tamanho;

    public Processo(String id, int tamanho) {
        this.id = id;
        this.tamanho = tamanho;
    }

    public String getId() {
        return id;
    }

    public int getTamanho() {
        return tamanho;
    }
}
