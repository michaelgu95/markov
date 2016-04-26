/**
 *
 * Main for Markov Text Generation Program
 *
 */
public class MarkovMain {
    public static void main(String[] args){
        IModel model = new WordMarkovModel();
        SimpleViewer view = new SimpleViewer("Comp 550 Markov Generation", "k count>");
        view.setModel(model);
    }
}
