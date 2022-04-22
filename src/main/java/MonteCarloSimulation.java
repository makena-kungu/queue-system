public class MonteCarloSimulation {

    public double pi() {
        var random = new CombinedLinearCongruential();
        var runs = 10934154;
        var radius = 1.0;

        var c = 0.0;//inside the circle
        var s = 0.0;//inside the square

        for (int i = 0; i < runs; i++) {
            var x = random.next();
            var y = random.next();

            var isInside = Math.pow(x, 2) + Math.pow(y, 2) <= radius;
            if (isInside) c++;
            else s++;
        }
        s += c;

        System.out.println("area of square = " + s);
        System.out.println("area of circle = " + c);

        //(area of circle/area of square) *4
        return c/s * 4;
    }
}
