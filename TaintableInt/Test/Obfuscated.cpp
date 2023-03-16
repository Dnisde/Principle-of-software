#include <iostream>
#include <string>
#include "TaintableInt.h"

using namespace std;
/*
 * You will see that there an unsigned array t[64] that is part of code.
 * our task is to taint the first 10 integers in t, 
 * and see what array entries end up tainted after you run: 
 * obfs( "94799382: EC500 is awesome!" ).
 */

void obfs(string ins) {
    
    unsigned x=0,a,i=1,j=0,t[64];
    int length = sizeof(t)/sizeof(t[0]);
    
    // Create a container(Array) to store the tainted integer.
    vector<TaintableInt> taint_arr;

    for(int i = 0; i < length; i++)
    {   
        // Taint the first 10 numbers within t[], and store the result into vectors.
        if (i < 10)
        {   
            TaintableInt new_taint;
            new_taint = t[i];
            TaintableInt::taint(new_taint);
            taint_arr.push_back(new_taint);
        }
        else
        {
            taint_arr.push_back(t[i]);
        }
    }

    // Run through the obfuscated code with Taintable Array:
    for(srand(0);i;) 
        j=j?--i<1&&a>>8?0:(putchar(i?(int) taint_arr[j]:a),i++)?taint_arr[j]=taint_arr[--i]+1,i>2?rand()%(i-2)+2:1:0
             :(26>((a=(++x<ins.length()?ins.at(x):EOF))|32)-97||1==a>>7)&&i<64?taint_arr[i+ins.length()]=taint_arr[i++]=a+taint_arr[0],0:1;
    
    for(int i=0; i < length; i++)
    {   
        // Output all the numbers with the label that who has been tainted.
        cout << taint_arr[i] << endl;
    }

}

void Test() {
    // untainted
    TaintableInt xx;
    xx = 2;
    cout << xx << endl;
    cout << 2 + 2 << endl;
    cout << 2 + xx << endl;
    cout << xx + 2 << endl << endl;

    // taint xx
    TaintableInt::taint(xx);
    cout << xx << endl;
    cout << 2 + 2 << endl;
    cout << xx + 2 << endl;
    cout << *xx + 2 << endl << endl;

    // more complex
    cout << xx++ << endl;
    cout << ++xx << endl;
    cout << 2 * xx << endl;
    cout << (2 - 3) + xx * 0 << endl;
    cout << xx + 0.3 << endl;
    cout << log2((double) xx) << endl;
    cout << (xx = xx) << endl;

}


int main()
{
    Test();

    string test = "94799382: EC500 is awesome!";
    // Execute TaintableInt for the obfs: 
    obfs(test);

    return 0;
    
}

   