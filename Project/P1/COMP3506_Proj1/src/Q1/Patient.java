//package Q1;

public class Patient extends PatientBase {

    /* final for minutes in an hour */
    private final int minutesInHour = 60;

    /* final for start time of the hospital in hours */
    private final int startTime = 8;

    /* final for lunch start time of the hospital in hours */
    private final int lunchStart = 12;

    /* final for the lunch end time of the hospital in hours */
    private final int lunchEnd = 13;

    /* final for the closing time of the hospital in hours */
    private final int endTime = 18;

    public Patient(String name, String time) {
        super(name, time);
    }

    @Override
    public int compareTo(PatientBase o) {
        /**
         * Compares this patient to the passed patient
         * @param o other patient to compare this patient with
         */
        int pb_hours = 0, pb_minutes = 0, o_hours = 0, o_minutes = 0;

        try {
            pb_hours = Integer.parseInt(this.getTime().substring(0,2));
            pb_minutes = Integer.parseInt(this.getTime().substring(3,5));

            o_hours = Integer.parseInt(o.getTime().substring(0,2));
            o_minutes = Integer.parseInt(o.getTime().substring(3,5));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


        // Convert all to the lowest units - minutes
        int pb_time = pb_hours * minutesInHour + pb_minutes;
        int o_time = o_hours * minutesInHour + o_minutes;

        return pb_time - o_time;
    }

    public boolean inStrictHospitalHours() {
        /**
         * Determines if this patient is booked within Strict Hospital hours (i.e. in 20 minute increments as well as
         * within hospital hours).
         * @returns true if patient is within 08:00 to 18:00 and outside 12:00 to 13:00 as well as on a 20-minute slot,
         * else false
         */

        int pb_hours = 0, pb_minutes = 0;

        try {
            pb_hours = Integer.parseInt(this.getTime().substring(0,2));
            pb_minutes = Integer.parseInt(this.getTime().substring(3,5));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return ((pb_hours >= this.startTime && pb_hours < lunchStart)
                || (pb_hours >= lunchEnd && pb_hours < endTime)) && (pb_minutes % 20 == 0);
    }

    public boolean inHospitalHours() {
        /**
         * Determines if this patient is booked within Hospital hours.
         * @returns true if patient is within 08:00 to 18:00 and outside 12:00 to 13:00, else false
         */
        int pb_hours = 0;

        try {
            pb_hours = Integer.parseInt(this.getTime().substring(0,2));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return pb_hours >= this.startTime && pb_hours < this.endTime;
    }
}
