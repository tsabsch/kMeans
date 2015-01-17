package Classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class YUSoMean implements IClassifier{

	private int k;
	//TODO maybe there is a way to store centroids as an array, not an arraylist
	private ArrayList<Instance<Double>> centroids;
	
	public YUSoMean(){
		k = 3;
		centroids = new ArrayList<Instance<Double>>();
	}
	
	public YUSoMean(int _k){
		k = _k;
		centroids = new ArrayList<Instance<Double>>();
	}
	
	@Override
	public String classify(Instance<Integer> i) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void learn(Trainingset<Integer> t){
		initializeCentroids(t);
		
		//TODO maybe we can use the Trainingset classes attribute to store the classification information
		Integer[] oldClassification = new Integer[t.size()];
		Integer[] newClassification = new Integer[t.size()];
		
		// while kMeans has not converged, do this
		int x = 0;
		do {			
			oldClassification = newClassification.clone();
			newClassification = new Integer[t.size()];
			
			System.out.println("Centroids: " + centroids);
			System.out.println("Classification: " + Arrays.toString(oldClassification));
			System.out.println(x);
			
			// iterate over all instances in the given trainingset
			for (int iIndex = 0; iIndex < t.size(); iIndex++) {
				Instance<Integer> instance = t.getInstance(iIndex);

				// compute distance to centroids
				double minDistance = Double.MAX_VALUE;
				int minCentroidIndex = -1;
				for (int cIndex = 0; cIndex < centroids.size(); cIndex++) {
					double dist = distance(centroids.get(cIndex), instance);
					if (dist < minDistance) {
						minDistance = dist;
						minCentroidIndex = cIndex;
					}
				}
				newClassification[iIndex] = minCentroidIndex;
			}
			
			// compute new centroids
			centroids = new ArrayList<Instance<Double>>();
			
			for (int cIndex = 0; cIndex < k; cIndex++) {
				
				//get the indexes of elements assigned to class i
				ArrayList<Integer> indexes = new ArrayList<Integer>();
				for (int iIndex = 0; iIndex < t.size(); iIndex++){
					if (newClassification[iIndex] == cIndex)
						indexes.add(iIndex);
				}
				//compute the new value
				ArrayList<Double> features = new ArrayList<Double>();				
				for (int fIndex = 0; fIndex < t.getFeatureCount(); fIndex++){
					int featureSum = 0;
					for (int iocIndex = 0; iocIndex < indexes.size(); iocIndex++)
						featureSum += t.getInstance(iocIndex).getFeature(fIndex);
					features.add(((double) featureSum / (double) indexes.size()));
				}
				centroids.add(new Instance<Double>(features, "-"));
			}
			x++;
		} while (x < 10 && !Arrays.equals(oldClassification, newClassification));
	}

	private void initializeCentroids(Trainingset<Integer> t){
		for (int i = 0; i < k; i++) {
			ArrayList<Double> features = new ArrayList<Double>();
			Random rand = new Random();
			for (int fIndex = 0; fIndex < t.getFeatureCount(); fIndex++){
				// compute a random feature value in between the feature space given by the trainingset
				List<Integer> list = Arrays.asList(t.getDomain(fIndex).values);
				features.add((double) rand.nextInt((Collections.max(list) - Collections.min(list) + 1) + Collections.min(list)));
			}
			centroids.add(new Instance<Double>(features, "-"));
		}
	}
	
	private double distance(Instance<Double> centroid, Instance<Integer> i){
		
		int distance = 0;
		for (int m = 0; m < centroid.getDimension(); m++) {
			distance += Math.abs(centroid.getFeature(m) - i.getFeature(m));
		}
		
		return distance;
	}
}
