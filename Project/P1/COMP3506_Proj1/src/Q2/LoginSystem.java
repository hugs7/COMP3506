//package Q2;

public class LoginSystem extends LoginSystemBase {
    /* Size of the hash table */
    public int M = 101;

    /* Hash constant */
    private final int c = 31;

    /* Load of the hash table */
    public int numUsers = 0;

    /* Array which imitates hash table functionality. Index is hash corresponding with email and value is tuple of email
    and hashed password. Default size is M. */
    public Tuple[] users = new Tuple[M];

    static class Tuple {
        /**
         * Class handling plain text email and password hash
         */

        /* Email in plain text */
        public String email;

        /* Hashed password */
        public int passwordHash;

        public Tuple(String email, int passwordHash) {
            this.email = email;
            this.passwordHash = passwordHash;
        }

        public String getEmail() {
            /**
             * Returns email in tuple
             */

            return this.email;
        }

        public int getPassword() {
            /**
             * Returns password in tuple
             */

            return this.passwordHash;
        }

        public void makeSentinel() {
            /**
             * Makes this tuple value sentinel
             */

            this.email = "SENTINEL";
            this.passwordHash = 0;
        }

        public void updatePassword(int newPasswordHash) {
            /**
             * Changes the password hash for this tuple. Does not check if user has permission to do so.
             */

            this.passwordHash = newPasswordHash;
        }
    }

    @Override
    public int size() {
        /**
         * Returns size of hash table
         */

        return this.M;
    }

    @Override
    public int getNumUsers() {
        /**
         * Returns number of users stored in the table. Not quite the load factor, but this can be calculated.
         */

        return this.numUsers;
    }

    @Override
    public int hashCode(String key) {
        /**
         * Computes hash function with compression as specified in the spec
         * @param key string to be converted into a hash code
         * @returns hash code compressed
         */

        // Compute hash function
        int hash = this.hashNoCompression(key);

        // Compression function
        return hash % this.M;
    }

    @Override
    public boolean addUser(String email, String password) {
        /**
         * Adds user into the hash table
         * @param email user's email in plain text
         * @param password user's password in plain text
         * @returns true if user can be inserted, false otherwise. User can't be inserted if they have a duplicate email
         */

        this.numUsers++;

        // Check for full table
        if (this.numUsers >= this.M) {
            // table full, triple table
            this.M *= 3;
            Tuple[] newUsers = new Tuple[this.M];
            // Recalculate hash values for all existing users.
            for (int i = 0; i <= this.M / 3; i++) {
                if (this.users[i] == null) {
                    continue;   // This position in array is empty.
                }
                // else there must be something at this position in this.users
                // new email hash and compression
                int emailHash = this.hashCode(this.users[i].getEmail());
                // Password hash remains the same (as no compression present)
                // Insert into newUsers array
                newUsers[emailHash] = new Tuple(this.users[i].getEmail(), this.users[i].getPassword());
            }
            this.users = newUsers;
        }

        // Hash and compress email
        int emailHash = this.hashCode(email);
//        System.out.println(email + ", " + emailHash);
        // Hash password (no compression as two distinct passwords would then have the same hash which is not ideal)
        int passwordHash = this.hashNoCompression(password);
        // --Add user--
        int i = emailHash;
        int found = -1;
        while (true) {
            // loop until we find a spot to put the new user
            if ((this.users[i] == null || this.users[i].getEmail().equals("SENTINEL")) && found == -1) {
                found = i;
            }
            if (found >= 0) {
                // Looking for duplicates
                if (this.users[i] == null) {
                    // end of checking
                    // put user into hash
                    this.users[found] = new Tuple(email, passwordHash);
                    break;
                } else if (this.users[i].getEmail().equals(email)) {
                    // Duplicate found. do not insert. Return false;
                    return false;
                }
            }
            // else, collision occurred and we can keep probing
            i++;
            i %= this.M;      // Ensure i loops around if necessary.
        }

        return true;
    }

    @Override
    public boolean removeUser(String email, String password) {
        /**
         * Removes user from the hash table. They must have the correct email and passworrd combination to be able to
         * remove themselves for security.
         * @param email user's email in plain text
         * @param password user's password in plain text
         * @returns true if user can be removed. False if they didn't get the credentials correct.
         */

        // Get hash for email so we can find it in the array
        int check = this.checkPassword(email, password);
        if (check < 0) {
            // user not in system, or incorrect password. Return false
            return false;
        }

        int emailHash = this.hashCode(email);
        // start at emailHash and linear probe until we find the correct address
        int i = emailHash;
        while(true) {
            if (this.users[i].getEmail().equals(email)) {
                // found user
                // replace with sentinel
                this.users[i].makeSentinel();
                return true;
            }

            if (i == emailHash - 1) {
                // we've looped around and haven't found it
                return false;
            }
            i++;
            i %= this.M;
        }
    }

    @Override
    public int checkPassword(String email, String password) {
        /**
         * Checks if a given email and password combination exist in the hash table. If true, returns the bucket index
         * where this user is stored in the hash table.
         * @param email user's email in plain text
         * @param password user's password in plain text
         * @returns if the user is not in the system -> return -1. if the user is in the system, but the password is
         *          incorrect -> return -2. If the user is in the system and the password is correct -> return the
         *          bucket index of the user in the hash table
         */

        int emailHash = this.hashCode(email);
        int i = emailHash;
        while(true) {
            if (this.users[i] == null) {
                return -1;
            }

            if (this.users[i].getEmail().equals(email)) {
                // found user in database.
                // Check password
                int passwordHash = this.hashNoCompression(password);
                if (passwordHash == this.users[i].getPassword()) {
                    // password matches
                    return i;
                }
                // else return -2
                return -2;
            }

            if (i == emailHash - 1) {
                // we've looped around, return -1
                return -1;
            }

            i++;
            i %= this.M;
        }
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        /**
         * Changes a password for a given email and password combination. User must have the correct oldPassword to
         * change their password
         * @param email user's email in plain text
         * @param oldPassword user's old password in plain text
         * @param newPassword user's new password in plain text
         * @returns true if email and oldPassword combination exist in the hash table and newPassword was updated.
         *          False otherwise.
         */

        int check = this.checkPassword(email, oldPassword);
        if (check < 0) {
            // user not in system, or incorrect password. Return false
            return false;
        }

        this.users[check].updatePassword(this.hashNoCompression(newPassword));

        return true;
    }

    public int hashNoCompression(String key) {
        /**
         * Calcualtes the hash code of a string without compressing the result by mod M
         * @Param key string to be hashed
         * @returns uncompressed hash code.
         */

        int numKeys = key.length();
        int[] A = new int[numKeys];

        // Convert string to ascii ints
        for (int i = 0; i < numKeys; i++) {
            A[i] = key.charAt(i);
        }

        // iteratively calculate hash code
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i] * Math.pow(this.c, A.length - i - 1);
        }

        return sum;
    }

//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
//        LoginSystem loginSystem = new LoginSystem();
//        assert loginSystem.hashCode("GQHTMP") == loginSystem.hashCode("H2HTN1");
//        assert loginSystem.size() == 101;
//
//        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
//        loginSystem.addUser("a@b.c", "L6ZS9");
//        loginSystem.addUser("H2HTN1", "L6ZS9");
//        loginSystem.addUser("GQHTMP", "L7ZS9");
//        System.out.println(loginSystem.users[94].getEmail());
//        System.out.println(loginSystem.users[16].getEmail());
//        System.out.println(loginSystem.users[17].getEmail());
//        System.out.println(loginSystem.checkPassword("H2HTN1", "L6ZS9"));
//        System.out.println("here");
//        System.out.println(loginSystem.checkPassword("GQHtMP", "L4ZS9"));
//        assert loginSystem.checkPassword("a@b.c", "ZZZZZZ") == -2;
//        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == 94;
//        loginSystem.removeUser("a@b.c", "L6ZS9");
//        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
//    }
}
