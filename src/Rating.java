public class Rating {
    private double rating = 0;
    private int nClientRate = 0;
    public Rating() {

    }

    public Rating(Rating mRating) {
        this.rating = mRating.getRating();
        this.nClientRate = mRating.getnClientRate();
    }
    public double getRating ( ) {
        return this.rating;
    }
    public int getnClientRate() {
        return this.nClientRate;
    }

    private void setRating (double rating) {
        this.rating = rating;
    }

    public void updateRating(double rating) {
        nClientRate++;
        double tmp = calculateRating(rating);
        setRating(tmp);
    }

    private double calculateRating(double rate) {
        double tmp = 0;
        tmp = rating * nClientRate;
        tmp += rate;
        return tmp/nClientRate;
    }

    public Rating clone() {
        return new Rating(this);
    }

}
