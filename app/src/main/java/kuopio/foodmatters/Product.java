package kuopio.foodmatters;

/**
 * Manage nutrional data for food item or complete meal.
 */
public class Product {
    /* Product name, e.g. "Meetvursti" */
    public String name;

    /* Manufacturer name, e.g. "Huhtahyvät" */
    public String manufacturer;

    /* Package size, e.g. 100 */
    public double size;

    /* Quantity, e.g. 5 */
    public double quantity;

    /* Unit symbol, e.g. "g" */
    public String unit;

    /* EAN-8 or EAN-13 barcode */
    public String barcode;

    /* Energy content in kcal */
    public double energy;

    /* Amount of fat in grams */
    public double fat;

    /* Amount of carbohydrates in grams */
    public double carbohydrate;

    /* Amount of sugars in grams */
    public double sugar;
     
    /*
     * Initialize object
     */
    public Product () {
        super ();

        name = "";
        manufacturer = "";
        size = 0.0;
        quantity = 0.0;
        unit = "";
        barcode = "";
        energy = 0.0;
        fat = 0.0;
        carbohydrate = 0.0;
        sugar = 0.0;
    }

    /*
     * Convert product to string
     */
    @Override
    public String toString () {
        /* Format quantity */
        String q;
        if (Math.abs (Math.round (this.quantity) % 1) < 0.000001) {
            /* Produce integer number, e.g. "1" */
            q = String.valueOf ((int) Math.round (this.quantity));
        } else {
            /* Produce decimal number, e.g. "1.5" */
            q = String.valueOf (this.quantity);
        }

        /* Format size */
        String s;
        if (Math.abs (Math.round (this.size) % 1) < 0.000001) {
            /* Produce integer number, e.g. "1" */
            s = String.valueOf ((int) Math.round (this.size));
        } else {
            /* Produce decimal number, e.g. "1.5" */
            s = String.valueOf (this.size);
        }

        /* Compute energy */
        double f;
        switch (this.unit) {
        case "l":
        case "kg":
            f = 10;
            break;

        case "ml":
        case "g":
            f = 0.01;
            break;

        case "pcs":
            f = 1.0;
            break;

        default:
            f = 0.0;
        }
        double energy = this.quantity * this.size * f * this.energy;

        /* Produce string representation of product */
        String name = q + " x "
            + this.manufacturer + " "
            + this.name + " "
            + s + " "
            + this.unit + " ("
            + String.valueOf ((int) Math.round (energy)) + " kcal)";
        return name;
    }
}

