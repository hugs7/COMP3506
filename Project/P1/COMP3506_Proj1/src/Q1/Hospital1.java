//package Q1;

import java.util.Iterator;

public class Hospital1 extends HospitalBase {
    public int appts_load;     // Number of appts stored in the array. Keeps track of index.
    public PatientBase[] appointments;      // array of Patients storing each appointment

    public Hospital1() {
        super();
        this.appointments = new PatientBase[1];
        this.appts_load = 0;
    }

    @Override
    public boolean addPatient(PatientBase patient) {
        /**
         * Inserts a patient to its appropriate index in the array, maintaining the order with respect to appointments.
         * Patients booked after the new patient are moved along one index in the array. If the array is full, an array
         * double the size is instantiated first and existing patients are copied across (in stable order).
         * @param patient: a new patient to be inserted into the array.
         * @returns False if patient is booking at an invalid time, or there already exists a patient booked at their
         *          request time. True otherwise.
         */


        // Check if patient is within hospital hours
        if (!((Patient) patient).inStrictHospitalHours()) {
            return false;   // Patient is trying to book at an invalid time
        }

        // Check for number of patients in the array and resize if needed
        if (this.appts_load + 1 >= this.appointments.length) {
            // Need to double the size of the array
            int new_length = this.appointments.length * 2;
            PatientBase[] new_appointments = new PatientBase[new_length];
            // Copy existing appointments across
            for (int i = 0; i < this.appointments.length; i++) {
                new_appointments[i] = this.appointments[i];
            }
            // Set appointments to new_appointments
            this.appointments = new_appointments;
        }

        // If empty array - base case
        if (this.appts_load == 0) {
            this.appointments[0] = patient;
            this.appts_load++;
            return true;
        }

        // Loop through array and determine spot to place new element
        int j = 0;      // Base case for inserting at start of array
        for (int i = this.appts_load - 1; i >= 0; i--) {
            int comp = patient.compareTo(this.appointments[i]);
            if (comp == 0) {
                return false;           // Duplicate not allowed
            } else if (comp > 0) {
                j = i + 1;
                break;
            }
        }

        // If no more elements in array
        if (j >= this.appts_load) {
            this.appointments[j] = patient;
            this.appts_load++;
            return true;
        }

        PatientBase pt = this.appointments[j];
        PatientBase nextPt;

        // insert patient at appointments[j]
        this.appointments[j] = patient;

        // Shift remaining elements along one index in the array
        for (int i = j + 1; i <= this.appts_load; i++) {
            // looping over future appts
            nextPt = this.appointments[i];
            this.appointments[i] = pt;
            pt = nextPt;
        }

        this.appts_load++;
        return true;

    }

    @Override
    public Iterator<PatientBase> iterator() {
        /**
         * Iterates over patient appointments in the array.
         */

        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                int n = appointments.length;
                return currentIndex < n && appointments[currentIndex] != null;
            }

            @Override
            public PatientBase next() {
                return appointments[currentIndex++];
            }
        };
    }

//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
//        var hospital = new Hospital1();
//        var p1 = new Patient("Max", "11:00");
//        var p2 = new Patient("Alex", "13:00");
//        var p3 = new Patient("George", "14:00");
//        hospital.addPatient(p1);
//        hospital.addPatient(p2);
//        hospital.addPatient(p3);
//        var patients = new Patient[] { p1, p2, p3 };
//        int i = 0;
//        for (var patient : hospital) {
//            if (!Objects.equals(patient, patients[i++])) {
//                System.err.println("Wrong patient encountered, check your implementation!");
//            }
//        }
//        System.out.println("Iterator time");
//        Iterator<PatientBase> it = hospital.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
//    }
}
