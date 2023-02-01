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
 * @tparam BASE_T The base type of the numerical part of the object
 */

template <typename BASE_T>
class Taintable {
public:

    /* Basic Constructor only: */

    /* Initialize Constructor */
    Taintable() : val(0), taint(false) {}

    /**
     * Constructs an untainted Taintable<BASE_T> from a given BASE_T
     * @param new_val The variable BASE_T from which to construct a Taintable object
     */
    explicit Taintable(BASE_T new_val) : val{new_val}, taint{false} {}

    /* default destructor */
    ~Taintable() = default;

public:

    /* Overload constructor only: */

    // Overload Operator: "*", for numerical value access operator
    const BASE_T operator* () const
    {
        return val;
    }

    // Overload Operator: "&", for Bool: tainted access operator
    const bool operator& () const
    {
        return taint;
    }

    /**
     * Overload Operator: "="
     * @param new_taint
     * @param num
     */
    Taintable<BASE_T>& operator= (const Taintable<BASE_T>& new_taint);

    Taintable<BASE_T>& operator= (const BASE_T& num);

    /**
     * Overload Operator: "+", For added up with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */

    // Define another template for applying the arithmetic object "+" Tainted object
    template <typename FIRST_OP> Taintable<BASE_T>& operator+ (FIRST_OP num) {
        Taintable<BASE_T> other_taint;
        other_taint.val = this->val + num;
        other_taint.taint = this->taint;
        return other_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic is not a Taintable object:
    template <typename FIRST_OP> friend Taintable<BASE_T>& operator+ (FIRST_OP num, Taintable<BASE_T> new_taint){
        return new_taint.operator+(num);
    }

    /**
     * Overload Operator: "-", For subtract with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    // Define another template for applying the arithmetic object "+" Tainted object
    template <typename FIRST_OP> Taintable<BASE_T>& operator- (FIRST_OP num) {
        Taintable<BASE_T> other_taint;
        other_taint.val = this->val - num;
        other_taint.taint = this->taint;
        return other_taint;
    }

    template <typename FIRST_OP> friend Taintable<BASE_T>& operator- (FIRST_OP num, Taintable<BASE_T> new_taint){
        return new_taint.operator-(num);
    }

    /**
     *  Overload Operator: "*":
     *  @param num
     *  @param other_taint
     *  First function: For multiply with another object
     *  Second function: For multiply with another Taintable object
     */
    template <typename FIRST_OP> Taintable<BASE_T>& operator* (FIRST_OP num) {
        Taintable<BASE_T> other_taint;
        other_taint.val = this->val * num;
        other_taint.taint = this->taint;
        return other_taint;
    }

    template <typename FIRST_OP> friend Taintable<BASE_T>& operator* (FIRST_OP num, Taintable<BASE_T> new_taint){
        return new_taint.operator*(num);
    }

    /**
     *  Overload Operator: "/":
     *  @param num
     *  @param other_taint
     *  First function: For multiply with another object
     *  Second function: For multiply with another Taintable object
     */
    template <typename FIRST_OP> Taintable<BASE_T>& operator/ (FIRST_OP num) {
        // Allocates and constructs an object of type Taintable<BASE_T>, and passing args to its constructor
        Taintable<BASE_T> other_taint;
        other_taint.val = this->val / num;
        other_taint.taint = this->taint;
        return other_taint;
    }

    template <typename FIRST_OP> friend Taintable<BASE_T>& operator/ (FIRST_OP num, Taintable<BASE_T> new_taint){
        return new_taint.operator/(num);
    }

    /**
     *  Overload Operator: "%":
     *  @param num
     *  @param other_taint
     *  First function: For multiply with another object
     *  Second function: For multiply with another Taintable object
     */
    template <typename FIRST_OP> Taintable<BASE_T>& operator% (FIRST_OP num) {
        Taintable<BASE_T> other_taint;
        other_taint.val = this->val % num;
        other_taint.taint = this->taint;
        return other_taint;
    }

    template <typename FIRST_OP> friend Taintable<BASE_T>& operator% (FIRST_OP num, Taintable<BASE_T> new_taint){
        return new_taint.operator%(num);
    }

    /**
     * Overload Operator: "++", prefix, ex: ++num
     * In this case, no specific type need to be specified, since only the value
     */
    Taintable<BASE_T> operator++ (int);

    /**
     * Overload Operator: "++", postfix, ex: num++,
     * In this case, no specific type need to be specified, since only the value
     */
    Taintable<BASE_T>& operator++ ();


    // Cannot defined at the outside of the class.
    friend ostream& operator<< (ostream &output, const Taintable<BASE_T> &new_taint) {

        if (new_taint.taint == true)
            output << new_taint.val << " [Tainted]";
        else
            output << new_taint.val;

        return output;
    }

    /**
     * Type casting(conversion) from object to primitive types
     * @return the following numerical type:
     */

    explicit operator const int() {return this->val;}

    explicit operator const float() { return this->val; }

    explicit operator const double() { return this->val; }

    explicit operator const long() { return this->vals; }

    // Return a taninable int which val equals the input reference: TaintableInt &tt
    static void tainted(Taintable<BASE_T>& new_taint);

private:
    BASE_T val;
    bool taint;

};

template<typename BASE_T>
Taintable<BASE_T>& Taintable<BASE_T>::operator=(const Taintable<BASE_T> &new_taint) {
    val = new_taint.val;
    taint = new_taint.taint;
    return *this;
}

template<typename BASE_T>
Taintable<BASE_T>& Taintable<BASE_T>::operator=(const BASE_T &num) {
    val = num;
    taint = false;
    return *this;
}

// Prefix++: ++i
template<typename BASE_T>
Taintable<BASE_T> Taintable<BASE_T>::operator++(int) {
    // Define a new instance that equals to the reference of the old TaintableInt
    Taintable<BASE_T> new_taint;
    new_taint.val = this->val;
    new_taint.taint = this->taint;
    // Reuse the operator to add-up the actual val of the Taintable Int
    ++*this;
    // return the new instance, which value equals the old TainttableInt (Value will be changed at the next access operation.)
    return new_taint;
}

// Postfix++: i++
template<typename BASE_T>
Taintable<BASE_T>& Taintable<BASE_T>::operator++() {
    val++;
    return *this;
}

template<typename BASE_T>
void Taintable<BASE_T>::tainted(Taintable<BASE_T> &new_taint) {
    new_taint.taint = true;
}

//
//template<typename FIRST_OP, typename BASE_T>
//Taintable<BASE_T>& operator+ (FIRST_OP num, Taintable<BASE_T> new_taint) {
//    // Using smart pointer
//    auto ans = std::make_shared<Taintable<BASE_T>>(num + new_taint.val, new_taint.taint);
//    return *ans;
//}
//
//template<typename FIRST_OP, typename BASE_T>
//Taintable<BASE_T>& operator- (FIRST_OP num, Taintable<BASE_T> new_taint) {
//    // Using smart pointer
//    auto ans = std::make_shared<Taintable<BASE_T>>(num - new_taint.val, new_taint.taint);
//    return *ans;
//}
//
//template<typename FIRST_OP, typename BASE_T>
//Taintable<BASE_T>& operator* (FIRST_OP num, Taintable<BASE_T> new_taint) {
//    // Using smart pointer
//    auto ans = std::make_shared<Taintable<BASE_T>>(num * new_taint.val, new_taint.taint);
//    return *ans;
//}
//
//template<typename FIRST_OP, typename BASE_T>
//Taintable<BASE_T>& operator/ (FIRST_OP num, Taintable<BASE_T> new_taint) {
//    // Using smart pointer
//    auto ans = std::make_shared<Taintable<BASE_T>>(num / new_taint.val, new_taint.taint);
//    return *ans;
//}
//
//template<typename FIRST_OP, typename BASE_T>
//Taintable<BASE_T>& operator% (FIRST_OP num, Taintable<BASE_T> new_taint) {
//    // Using smart pointer
//    auto ans = std::make_shared<Taintable<BASE_T>>(num % new_taint.val, new_taint.taint);
//    return *ans;
//}

#endif //HW0_12_TAINTABLE_H
