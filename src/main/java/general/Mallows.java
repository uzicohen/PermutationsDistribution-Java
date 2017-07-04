package general;

import java.util.ArrayList;

public class Mallows {
	private ArrayList<String> modal;

	private double phi;

	private double z;

	public Mallows(ArrayList<String> modal, double phi) {
		this.modal = modal;
		this.phi = phi;
		this.z = 1.0;
		for (int i = 0; i < modal.size(); i++) {
			double current = 1.0;
			for (int j = 0; j < i; j++) {
				current += Math.pow(this.phi, j + 1);
			}
			this.z *= current;
		}
	}

	public ArrayList<String> getModal() {
		return this.modal;
	}

	public double getPhi() {
		return this.phi;
	}

	public double getZ() {
		return z;
	}

	public void setModal(ArrayList<String> modal) {
		this.modal = modal;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
