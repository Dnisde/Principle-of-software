// Copyright 2022 Zhaozhong Qi zqi5@bu.edu

/* Useful Reference: 
 * 1. Operator Overloading basic tutorial from: https://www.runoob.com/cplusplus/cpp-overloading.html
 * 2. Operator Overloading basic tutorial from: https://docs.microsoft.com/en-us/cpp/cpp/operator-overloading?view=msvc-170
 * 3. Data Conversion in C++: https://www.geeksforgeeks.org/data-conversion-in-c/
 * 4. Type Casting reference: https://cplusplus.com/doc/oldtutorial/typecasting/
 * 5. Pointer Reference: Shallow copy and Deep Copy in C++: https://www.geeksforgeeks.org/shallow-copy-and-deep-copy-in-c/
 * 6. Another good example of showing Shallow copy and Deep copy: https://segmentfault.com/a/1190000021164374
 */

#ifndef TAINTABLEINT_H_
#define TAINTABLEINT_H_
#endif

#include <vector>
#include <cmath>
#include <typeinfo>

using namespace std;

 /*
  * Your first task is to design and implement a class TaintableInt that represents an integer that may or may not be tainted. 
  * Taint flows from any tainted integer to results of operations using that integer, where possible (but at least including the basic arithmetic operations: +,-,*,/,%)). 
  * For example, if xx is tainted, then so is xx + 2.
  * 
  * Your implementation should otherwise behave like an int, to the extent possible, but have the following methods for a TaintableInt tt :
  * &t returns true iff the integer is tainted.
  * *t returns the integer associated with t.
  * taint(t) causes t to be tainted.
  */

class TaintableInt {

	public: 
        // Constructord
		TaintableInt();
        // Desctuctor
        ~TaintableInt();
        // Type casting from other type to the Taintable Int:
        TaintableInt(int num);

    public: 
        // Overload Operator: "+"
        // For added up with another Integer.
        TaintableInt operator+ (const int &num); 
        // For added up with another floatting(64bit) number
        TaintableInt operator+ (const double &num); 
        // For added up with another Taintable Integer.
        TaintableInt operator+ (const TaintableInt &tt);
        
        friend TaintableInt operator+ (const int &num, 
                                        TaintableInt tt);

        // Advance: Overload Operator: "-": 

        // Overload Operator: "*":
        // For added up with another Integer.
        TaintableInt operator* (const int &num); 
        // For added up with another Taintable Integer.
        TaintableInt operator* (const TaintableInt &tt);
        // Add up by different order.
        friend TaintableInt operator* (const int &num, 
                                        TaintableInt tt);

        // Advance: Overload Operator: "/":

        // Overload Operator: "++", postfix, ex: num++
        TaintableInt operator++ ();
        // Overload Operator: "++", prefix, ex: ++num
        TaintableInt operator++ (int);

        // Overload Operator: "="
        TaintableInt operator= (const int &num);

        // Overload Operator: "=", for unsigned int
        TaintableInt operator= (const unsigned int &num);

        // Overload Operator: "*", for member access operator
        TaintableInt operator* () const
        {
            int new_val = *val;
            return new_val;
        };

        friend ostream &operator<< (ostream &output,
                                    const TaintableInt &tt)
        {   
            if (tt.t == true) 
                output << tt.val[0] << " [Tainted]";
            else
                output << tt.val[0];
            
            return output;
        };

        // Cast type from Taintable int to double():
        explicit operator double();

        // Cast type from Taintable int to xx:
        explicit operator int();
        
        // Return a taninable int which val equals the input reference: TaintableInt &tt
        static void taint(TaintableInt &tt);
        
	private:
    
		int *val;
        unsigned int *unsigned_val;
        bool t;

};

TaintableInt::TaintableInt(void) 
{
    // cout <<  "DEBUG: Object is being created." << endl;
    val = new int();
    *val = 0;
    unsigned_val = new unsigned int();
    *unsigned_val = 0;
    t = false;
};

TaintableInt::~TaintableInt(void) 
{
    // cout <<  "DEBUG: Object is being deleted." << endl;
};

TaintableInt::TaintableInt(int num) 
{   
    val = new int();
    *val = num;
};

TaintableInt TaintableInt::operator= (const int &num) 
{
    delete[] val;
    val = new int(sizeof(num));
    *val = num;
    return *this;
};

TaintableInt TaintableInt::operator= (const unsigned int &num) 
{
    delete[] unsigned_val;
    unsigned_val = new unsigned int(sizeof(num));
    *unsigned_val = num;
    return *this;
};

TaintableInt TaintableInt::operator+ (const int& num) 
{   
    TaintableInt new_taint;
    new_taint.val[0] = this->val[0] + num;
    new_taint.t = this->t;
    return new_taint;
};

TaintableInt TaintableInt::operator+ (const double &num)
{
    // C++ Type casting from double() to int() always rounds down, 
    // Even if the fraction part is 0.99999999.
    TaintableInt new_taint;
    new_taint.val[0] = double(this->val[0]) + num;
    new_taint.t = this->t;
    return new_taint;
};

TaintableInt TaintableInt::operator+ (const TaintableInt &tt) 
{   
    TaintableInt new_taint;
    new_taint.val[0] = this->val[0] + tt.val[0];
    new_taint.t = tt.t;
    return new_taint;
};

TaintableInt operator+ (const int &num, TaintableInt tt)
{   
    // Reuse the operator to enable the different way of Add-up
    return tt.operator+(num);
};

TaintableInt TaintableInt::operator* (const int& num) 
{   
    TaintableInt new_taint;
    new_taint.val[0] = this->val[0] * num;
    new_taint.t = this->t;
    return new_taint;
};

TaintableInt TaintableInt::operator* (const TaintableInt &tt) 
{   
    TaintableInt new_taint;
    new_taint.val[0] = this->val[0] * tt.val[0];
    new_taint.t = tt.t;
    return new_taint;
};

TaintableInt operator* (const int &num, TaintableInt tt)
{  
    // Reuse the operator to enable the different way of Multiply
    return tt.operator*(num);
};

TaintableInt TaintableInt::operator++ ()
{
    val[0]++;
    return *this;
};

TaintableInt TaintableInt::operator++ (int)
{   
    // Define a new instance that equals to the reference of the old TaintableInt
    TaintableInt new_taint;
    new_taint.val[0] = this->val[0];
    new_taint.t = this->t;
    // Reuse the operator to add-up the actual val of the Taintable Int
    ++*this;
    // return the new instance, which value equals the old TainttableInt (Value will be changed at the next access operation.)
    return new_taint;
};

TaintableInt::operator double()
{
    double new_val = val[0];
    return new_val;
};

TaintableInt::operator int()
{
    int new_val = unsigned_val[0];
    return new_val;
};

void TaintableInt::taint(TaintableInt &tt) 
{   
    // Taint the TaintableInt:
    tt.t = true;
};