//
// Copyright 2022 Zhaozhong Qi zqi5@bu.edu
//

#ifndef HW0_12_TAINTABLE_H
#define HW0_12_TAINTABLE_H

#include <iostream>
#include <cmath>
#include <typeinfo>

using namespace std;

/**
 * A generalization of {@link TaintableInt} to any of the standard numeric datatypes.
 * This (templated) class represents a Taintable<BASE_T> base class (BASE_T).
 * For example, Taintable<int> should be equivalent to the TaintableInt class.
 * @tparam BASE_T The base type of the
 */

template <typename BASE_T>

class Taintable {
public:

    /* Basic Constructor only: */

    /* default constructor */
    Taintable();

    /**
     * Assign a object to become a Taintable<BASE_T> object:
     * @param num
     * @param taint The variable which direct an Taintable object is tainted or not
     */
    Taintable(BASE_T num, bool new_taint);

    /**
     * Constructs an untainted Taintable<BASE_T> from a given BASE_T
     * @param new_val The variable BASE_T from which to construct a Taintable object
     */
    Taintable(BASE_T new_val);

    Taintable(Taintable& other);

    /* default destructor */
    ~Taintable();

public:

    /* Overload constructor only: */

    // Overload Operator: "="
    Taintable<BASE_T>& operator= (const Taintable<BASE_T>& new_taint);

    Taintable<BASE_T>& operator= (const BASE_T &num);

    /**
     * Overload Operator: "+", For added up with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    Taintable<BASE_T> operator+ (BASE_T &num);

    Taintable<BASE_T> operator+ (Taintable<BASE_T> &other_taint);

    friend Taintable<BASE_T> operator+ (BASE_T &num, Taintable<BASE_T> other_taint);

    // Overload Operator: "-":

    /**
     *  Overload Operator: "*":
     *  @param num
     *  @param other_taint
     *  First function: For multiply with another object
     *  Second function: For multiply with another Taintable object
     */
    // For added up with another Integer.
    Taintable<BASE_T> operator* (BASE_T &num);
    // For added up with another Taintable Integer.
    Taintable<BASE_T> operator* (Taintable<BASE_T> &other_taint);
    // Add up by different order.
    friend Taintable<BASE_T> operator* (BASE_T &num, Taintable<BASE_T> other_taint);

    // Advance: Overload Operator: "/":

    // Overload Operator: "++", postfix, ex: num++
    Taintable &operator++ ();
    // Overload Operator: "++", prefix, ex: ++num
    Taintable<BASE_T> operator++ (int);

    // Overload Operator: "*", for member access operator
    Taintable operator* () const;

    // Overload Operator: "&", for member access operator
    Taintable operator& () const;

    friend ostream &operator<< (ostream &output, const Taintable<BASE_T> &new_taint);

    /**
     * Type casting(conversion) from object to primitive types
     * @return
     */

    explicit operator const int() {return operator*();}

    explicit operator const float() { return operator*(); }

    explicit operator const double() { return operator*(); }

    explicit operator const long() { return operator*(); }

    // Return a taninable int which val equals the input reference: TaintableInt &tt
    static void tainted(Taintable<BASE_T>& new_taint);

private:

    BASE_T *val;
    bool taint;

};

template<typename BASE_T>
Taintable<BASE_T>::Taintable() {   // Initialize
    val = new int();
    *val = 0;
    taint = false;
}

template<typename BASE_T>
Taintable<BASE_T>::Taintable(BASE_T num, bool new_taint) {
    val = new int();
    *val = num;
    taint = new_taint;
}

template<typename BASE_T>
Taintable<BASE_T>::Taintable(BASE_T new_val) {
    val = new int();
    *val = new_val;
    taint = false;
}

template<typename BASE_T>
Taintable<BASE_T>::Taintable(Taintable &other) {
    val = new int();
    *val = other.val[0];
    taint = other.taint;
}

template<typename BASE_T>
Taintable<BASE_T>::~Taintable() {
    // cout <<  "DEBUG: Object is being deleted." << endl;
}

template<typename BASE_T>
Taintable<BASE_T> &Taintable<BASE_T>::operator=(const Taintable<BASE_T> &new_taint) {
    delete[] val;
    val = new int(sizeof(new_taint.val[0]));
    *val = new_taint.val[0];
    taint = new_taint.taint;
    return *this;
}

template<typename BASE_T>
Taintable<BASE_T> &Taintable<BASE_T>::operator=(const BASE_T &num) {
    delete[] val;
    val = new int(sizeof(num));
    *val = num;
    taint = false;
    return *this;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator+(BASE_T &num) {
    Taintable<BASE_T> new_taint;
    new_taint.val[0] = this->val[0] + num;
    new_taint.taint = this->taint;
    return new_taint;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator+(Taintable<BASE_T> &other_taint) {
    Taintable<BASE_T> new_taint;
    new_taint.val[0] = this->val[0] + other_taint.val[0];
    new_taint.taint = other_taint.taint;
    return new_taint;
}

template<typename BASE_T>
Taintable<BASE_T> operator+(BASE_T &num, Taintable<BASE_T> other_taint) {
    // Reuse the operator to enable the different way of Add-up
    return other_taint.operator+(num);
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator*(BASE_T &num) {
    Taintable<BASE_T> new_taint;
    new_taint.val[0] = this->val[0] * num;
    new_taint.taint = this->taint;
    return new_taint;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator*(Taintable<BASE_T> &other_taint) {
    Taintable<BASE_T> new_taint;
    new_taint.val[0] = this->val[0] * other_taint.val[0];
    new_taint.taint = other_taint.taint;
    return new_taint;
}

template<typename BASE_T>
Taintable<BASE_T> operator*(BASE_T &num, Taintable<BASE_T> other_taint) {
    // Reuse the operator to enable the different way of Multiply
    return other_taint.operator*(num);
}

template<typename BASE_T>
Taintable<BASE_T> &Taintable<BASE_T>::operator++() {
    val[0]++;
    return *this;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator++(int) {
    // Define a new instance that equals to the reference of the old TaintableInt
    Taintable<BASE_T> new_taint;
    new_taint.val[0] = this->val[0];
    new_taint.taint = this->taint;
    // Reuse the operator to add-up the actual val of the Taintable Int
    ++*this->val[0];
    // return the new instance, which value equals the old TainttableInt (Value will be changed at the next access operation.)
    return new_taint;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator*() const {
    return *val;
}

template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator&() const {
    return taint;
}

template<typename BASE_T>
ostream &operator<<(ostream &output, const Taintable<BASE_T> &new_taint) {
    if (new_taint.taint == true)
        output << new_taint.val[0] << " [Tainted]";
    else
        output << new_taint.val[0];

    return output;
}

template<typename BASE_T>
void Taintable<BASE_T>::tainted(Taintable<BASE_T> &new_taint) {
    new_taint.taint = true;
}


#endif //HW0_12_TAINTABLE_H
