

import java.util.Random;
public  class RandomGaussian {
  
  public static void RandomGaussian(String... aArgs){
    RandomGaussian gaussian = new RandomGaussian();
    double MEAN = 100.0f; 
    double VARIANCE = 5.0f;
    for (int idx = 1; idx <= 10; ++idx){
      log("Generated : " + gaussian.getGaussian(MEAN, VARIANCE));
    }
  }
    
  private Random fRandom = new Random();
  
  double getGaussian(double aMean, double aVariance){
    return aMean + fRandom.nextGaussian() * aVariance;
  }

  private static void log(Object aMsg){
    System.out.println(String.valueOf(aMsg));
  }
}
