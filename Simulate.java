public class Simulate{
      public static void main(String[] args){
        Player p = new Player(10000);
        double[] g = new double[4];
        g[0]=-0.51066;
        g[1] = 0.760666;
        g[2]= -0.35663;
        g[3] = -.184483;
      p.setGenome(g);
      double fitness = p.simulate("on");
      System.out.println(fitness);

}
}
