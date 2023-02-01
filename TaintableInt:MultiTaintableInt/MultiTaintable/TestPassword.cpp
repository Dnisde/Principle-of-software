//
// Copyright 2022 Zhaozhong Qi zqi5@bu.edu
//

#include "MultiTaintable.h"
#include <array>

const int SIZE = 22;
const int MAX = 10082 ;
const int LARGE_PRIME = 524287;

using namespace std;

std::array<MultiTaintable<int, string>, SIZE> theHash(std::array<MultiTaintable<int, string>, SIZE> &input, bool taint) {
    std::array<MultiTaintable<int, string>, SIZE> result(input);
    if (taint) {
        for (int i = 0; i < SIZE; i++) {
            /**
             * Suppose we enter a random password, " random_password "
             * Because at the following shown, there is a specific unknown hash function might decrypt our password to a specific form.
             * (Not traceable).
             * We tried to use our Multi-Taintable to "label" each character within our password,
             * which by a symbol "tt" follows by the index of that character been specified in the password.
             * Then, after the unknown hash function, we will be able to know where they locates, and how they were being hashed.
             */
            MultiTaintable<int, string>::taint(result[i], "tt" + to_string(i));
        }
    }
    // An unknown hash function:
    int num = 1;
    for (int ii = 0; ii < SIZE * SIZE; ii++) {
        switch (num % 3) {
            case 0:
            case 1:
                result[num % SIZE] = (result[num % SIZE] + 1) % MAX;
                break;
            case 2:
                result[num % SIZE] = (result[num % SIZE] + result[(3 * num) % SIZE]) % MAX;
                break;
        }
        num = (num * 3) % LARGE_PRIME;
    }

    // After the hash function being implemented,
    // However, it returns a new result, but remained by original result character being tainted.
    return result;
}

void testPassword() {
    // enter the password
    string password;
    cout << "Please enter the password: " << endl;
    cin >> password;

    // convert to a series of integers by a taintable property
    std::array<MultiTaintable<int, string>, SIZE> passInts;
    for (int ii = 0; ii < SIZE; ii++)
        passInts[ii] = (int) password.at(ii%password.length());

    // compute the hash
    bool TAINT = true;
    std::array<MultiTaintable<int, string>, SIZE> hashed = theHash(passInts, TAINT);

    /**
      * After the label, the return value will be all the referenced index symbols that the Hash function affected.
      * Now, let's get started to test the password based on all those symbols!
      * @return: All the referenced symbols
      */
    if (TAINT) {
        string symbols = MultiTaintable<int, string>::get_Unique_symbols(hashed[5]);
        cout << "\n" << symbols << endl;
    }

    /**
     * In my case, the get_Unique_symbols function returns a result:
     *      "All referenced symbols: tt5 tt15 tt1 tt3 tt9"
     * In this case, that represent no matter what string of password we entered,
     * The extended password (SIZE 22) is only refer about those indexes of type.
     * Thus, we using brute force to loop all the possible cases of the password.
     */

    string correct_pwd = "";
    for(int i = 0; i < 9; i++)
    {
        for(int j = 0; j < 9; j++)
        {
            for(int k = 0; k < 9; k++)
            {
                for(int l = 0; l < 9; l++) {
                    for (int m = 0; m < 9; m++) {
                        string new_pwd = "r" + to_string(i) + "a" + to_string(j) + "n" +
                                         to_string(k) + "dom" + to_string(l) + "passw" + to_string(m) + "ord";
                        std::array<MultiTaintable<int, string>, SIZE> new_passInts;
                        for (int ii = 0; ii < SIZE; ii++)
                            new_passInts[ii] = (int) new_pwd.at(ii%new_pwd.length());
                        array<MultiTaintable<int, string>, SIZE> new_hashed = theHash(new_passInts, false);
                        if (new_hashed[5] == 10) {
                            cout << "Correct match of Pass_Ints: ";
                            cout << new_pwd << endl;
                            correct_pwd = new_pwd;
                            break;

                        }
                    }
                }
            }
        }
    }

    // Test password:
    // convert to a series of integers by a taintable property
    string test_pwd = correct_pwd;
    array<MultiTaintable<int, string>, SIZE> _passInts;
    for (int ii = 0; ii < SIZE; ii++) {
        _passInts[ii] = (int) test_pwd.at(ii % test_pwd.length());
    }
    array<MultiTaintable<int, string>, SIZE> hash_pass = theHash(_passInts, false);
    if (hash_pass[5] == 10)
        cout << "You may enter!" << endl;
    else
        cout << "Sorry - no entry" << endl;

}


int main()
{
    testPassword();
    return 0;
}

