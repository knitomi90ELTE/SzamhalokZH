package szamhalokzh;

public class Card {

    public String name;
    public int value;

    public Card(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Card{" + "name=" + name + ", value=" + value + '}';
    }

    public String asMessage() {
        return name + "," + value;
    }

}
