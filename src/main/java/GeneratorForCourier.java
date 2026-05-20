public class GeneratorForCourier { public static Courier getRandomCourier() {
    String unique = String.valueOf(System.currentTimeMillis());
    return new Courier(
            "courier_" + unique,
            "password_" + unique,
            "name_" + unique
    );
}
}
