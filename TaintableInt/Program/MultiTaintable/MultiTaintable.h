//
// Copyright 2022 Zhaozhong Qi zqi5@bu.edu
//

/**
 * A generalization of {@link MultipleTaintable} to any of the standard numeric datatype and other type.
 * This (templated) class represents a MultipleTaintable<BASE_T, OTHER_T> base class.
 * For example, MultipleTaintable<int, string> should be equivalent to the MultipleTaintable class.
 * @tparam BASE_T The base type of the numerical part of the object
 * @tparam OTHER_T The base type of the Non-arithmetic part (character(String) of the object
 */

#ifndef HW0_12_MULTITAINTABLE_H
#define HW0_12_MULTITAINTABLE_H
#include <iostream>
#include <cmath>
#include <vector>
#include <algorithm> // In order to apply std::vector::find();
#include <string>

using namespace std;

template <typename BASE_T, typename OTHER_T>
class MultiTaintable {

public:
    /* Basic Constructor only: */

    /* Initialize Constructor */
    MultiTaintable() : numerical(0), tainted(false) {
        character = vector<OTHER_T>();
    }

    /**
     * Assign a object to become a MultiTaintable<BASE_T> object:
     * @param num The variable which direct the arithmetic value of the MultiTaintable Object.
     * @param non_num The variable which direct the non-arithmetic value of the MultiTaintable Object.
     * @param taint The variable which direct an MultiTaintable object is tainted or not.
     */
    MultiTaintable(BASE_T num, OTHER_T non_num, bool new_taint) : numerical{num}, character{character.push_back(non_num)}, tainted{new_taint} {}

    /**
     * Assign a object to become a MultiTaintable<BASE_T> object:
     * @param num
     * @param taint The variable which direct an MultiTaintable object is tainted or not
     */
    MultiTaintable(BASE_T num, OTHER_T str) : numerical{num}, character{character.push_back(str)}, tainted{false} {}

    /**
     * Constructs an Taintable<BASE_T> from a given BASE_T
     * @param new_val The variable BASE_T from which to construct a MultiTaintable object
     */
    explicit MultiTaintable(const BASE_T new_val) : numerical{new_val}, tainted{false} {}

    /* default destructor */
    ~MultiTaintable() = default;

public:

    // Overload Operator: "*", for numerical value access operator
    const BASE_T operator* () const
    {
        return numerical;
    }

    // Overload Operator: "&", for Bool: tainted access operator
    const OTHER_T operator& () const
    {
        // DEBUG:
        OTHER_T new_obj = "[" + get_element(*this) + "]";
        return new_obj;
    }

    /**
     * Operator"<<" overloading function, Direct access the value of the rhs operand,
     * and insert them respectively to the output stream.
     * @param output Output stream of the C++
     * @param rhs_taint Operand of the right hand side of the operator
     * Note: Cannot defined at the outside of the class.
     * @return the stream of the output of both numerical and non-numerical part of the object
     */
    friend ostream& operator<< (ostream &output, const MultiTaintable<BASE_T, OTHER_T>& rhs_taint) {
        if (rhs_taint.tainted == true)
            output << rhs_taint.numerical << " [" << get_element(rhs_taint) << "]";
        else
            output << rhs_taint.numerical;

        return output;
    }

    /**
     * Equality check between two MultiTaintedInt object.
     * @param lhs The left hand side operand that is being checked .
     * @param rhs The right hand side operand (number only) that is being checked.
     * @return TRUE if and only if lhs and rhs have represent the same numerical value, otherwise FALSE (irrespective taint object).
     */
    friend bool operator==(const MultiTaintable<BASE_T, OTHER_T> lhs, const BASE_T& rhs) {
        return *lhs == rhs;
    }

    /**
     * Equality check between two MultiTaintedInt object.
     * @param lhs The left hand side operand that is being checked .
     * @param rhs The right hand side operand that is being checked.
     * @return TRUE if and only if lhs and rhs have represent the same numerical value, otherwise FALSE.
     */
    friend bool operator==(const MultiTaintable<BASE_T, OTHER_T> lhs, const MultiTaintable<BASE_T, OTHER_T> rhs) {
        return *lhs == *rhs;
    }

    /**
     * Overload Operator: "="
     * @param new_taint
     * @param num
     */
    MultiTaintable<BASE_T, OTHER_T>& operator= (const MultiTaintable<BASE_T, OTHER_T>& new_taint)
    {
        numerical = new_taint.numerical;
        tainted = new_taint.tainted;
        character = new_taint.character;
        return *this;
    }

    MultiTaintable<BASE_T, OTHER_T>& operator= (const BASE_T& num)
    {
        numerical = num;
        tainted = false;
        return *this;
    }

    /**
     * Implicit conversion from object to primitive types
     */
    explicit operator const int() { return operator*(); }
    explicit operator const long() { return operator*(); }
    explicit operator const float() { return operator*(); }
    explicit operator const double() { return operator*(); }

    /**
     * Overload Operator: "+", For added up with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    template <typename FIRST_OP> MultiTaintable<BASE_T, OTHER_T>& operator+ (const FIRST_OP num) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical + num;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another template for applying the arithmetic object "+" Tainted object
    MultiTaintable<BASE_T, OTHER_T>& operator+ (const MultiTaintable<BASE_T, OTHER_T> other_taint) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical + other_taint.numerical;
        new_taint.tainted = this->tainted;
        this->character.insert(this->character.end(), other_taint.character.begin(), other_taint.character.end());
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic is not a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator+ (FIRST_OP num,
                                                        MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator+(num);
    }

    // Define another friendly function to deal with the first operand in arithmetic is a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator+ (MultiTaintable<BASE_T, OTHER_T> this_taint,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator+(this_taint);
    }

    /**
     * Overload Operator: "-", For subtract with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    template <typename FIRST_OP> MultiTaintable<BASE_T, OTHER_T>& operator- (const FIRST_OP num) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical - num;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another template for applying the arithmetic object "-" Tainted object
    MultiTaintable<BASE_T, OTHER_T>& operator- (const MultiTaintable<BASE_T, OTHER_T> other_taint) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical - other_taint.numerical;
        new_taint.tainted = this->tainted;
        this->character.insert(this->character.end(), other_taint.character.begin(), other_taint.character.end());
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic "-" is not a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator- (FIRST_OP num,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator-(num);
    }

    // Define another friendly function to deal with the first operand in arithmetic "-" is a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator- (MultiTaintable<BASE_T, OTHER_T> this_taint,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator-(this_taint);
    }

    /**
     * Overload Operator: "*", For multiply with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    template <typename FIRST_OP> MultiTaintable<BASE_T, OTHER_T>& operator* (const FIRST_OP num) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical * num;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another template for applying the arithmetic object "*" Tainted object
    MultiTaintable<BASE_T, OTHER_T>& operator* (const MultiTaintable<BASE_T, OTHER_T> other_taint) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical * other_taint.numerical;
        new_taint.tainted = this->tainted;
        this->character.insert(this->character.end(), other_taint.character.begin(), other_taint.character.end());
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic "*" is not a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator* (FIRST_OP num,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator*(num);
    }

    // Define another friendly function to deal with the first operand in arithmetic "*" is a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator* (MultiTaintable<BASE_T, OTHER_T> this_taint,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator*(this_taint);
    }

    /**
     * Overload Operator: "/", For arithmetic divide by another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    template <typename FIRST_OP> MultiTaintable<BASE_T, OTHER_T>& operator/ (const FIRST_OP num) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical / num;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another template for applying the arithmetic object "/" Tainted object
    MultiTaintable<BASE_T, OTHER_T>& operator/ (const MultiTaintable<BASE_T, OTHER_T> other_taint) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical / other_taint.numerical;
        new_taint.tainted = this->tainted;
        this->character.insert(this->character.end(), other_taint.character.begin(), other_taint.character.end());
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic "/" is not a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator/ (FIRST_OP num,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator/(num);
    }

    // Define another friendly function to deal with the first operand in arithmetic "/" is a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator/ (MultiTaintable<BASE_T, OTHER_T> this_taint,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator/(this_taint);
    }

    /**
     * Overload Operator: "%", To get remainder with another Integer.
     * @param num
     * @param other_taint
     * First function: For added up with another object
     * Second function: For added up with another Taintable object
     */
    template <typename FIRST_OP> MultiTaintable<BASE_T, OTHER_T>& operator% (const FIRST_OP num) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical % num;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another template for applying the arithmetic object "-" Tainted object
    MultiTaintable<BASE_T, OTHER_T>& operator% (const MultiTaintable<BASE_T, OTHER_T> other_taint) {
        static MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical % other_taint.numerical;
        new_taint.tainted = this->tainted;
        this->character.insert(this->character.end(), other_taint.character.begin(), other_taint.character.end());
        new_taint.character = this->character;
        return new_taint;
    }

    // Define another friendly function to deal with the first operand in arithmetic "-" is not a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator% (FIRST_OP num,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator%(num);
    }

    // Define another friendly function to deal with the first operand in arithmetic "-" is a Taintable object:
    template <typename FIRST_OP> friend MultiTaintable<BASE_T, OTHER_T>& operator% (MultiTaintable<BASE_T, OTHER_T> this_taint,
                                                                                    MultiTaintable<BASE_T, OTHER_T> other_taint) {
        return other_taint.operator%(this_taint);
    }

    /**
     * Prefix++: ++i
     * Define a new instance which numerical value incremented by 1 after the output stream complied.
     * (Numerical value will be changed at the next access operation.)
     */

    MultiTaintable<BASE_T, OTHER_T> operator++(int) {

        MultiTaintable<BASE_T, OTHER_T> new_taint;
        new_taint.numerical = this->numerical;
        new_taint.tainted = this->tainted;
        new_taint.character = this->character;
        // Reuse the operator to add-up the actual val of the Taintable Int
        ++*this;
        // return the new instance, which value equals the old TainttableInt (Value will be changed at the next access operation.)
        return new_taint;
    }

    /**
     * Postfix++: i++
     * Define a new instance which numerical value incremented by 1 before the output stream complied.
     */
    MultiTaintable<BASE_T, OTHER_T>& operator++() {
        numerical++;
        return *this;
    }

public:

    // Member function:

    /* Define a function to access all the tainted variable within the vector: */
    friend OTHER_T get_element(const MultiTaintable<BASE_T, OTHER_T>& new_taint)
    {
        // initialize as a string, and concatenate it as a string.
        OTHER_T a = " ";
        for(int i = 0; i < new_taint.character.size(); i++)
        {
            a = a + new_taint.character[i] + " ";
        }
        return a;
    }

    static OTHER_T get_Unique_symbols(MultiTaintable<BASE_T, OTHER_T>& new_taint)
    {
        OTHER_T symbol_str = "";
        for(auto i = new_taint.character.begin(); i!= new_taint.character.end(); ++i)
        {
            // Find non-duplicate "tained" symbols among all tainted object.
            if (find(new_taint.symbols.begin(), new_taint.symbols.end(), *i) != new_taint.symbols.end()) continue;
            new_taint.symbols.push_back(*i);
            symbol_str = symbol_str + *i + " ";
        }
        return "All referenced symbols: " + symbol_str;
    }

    /* Return a taninable int which val equals the input reference: TaintableInt &tt */
    static void taint(MultiTaintable<BASE_T, OTHER_T>& new_taint, const OTHER_T& new_string) {
        new_taint.character.push_back(new_string);
        new_taint.tainted = true;
    }


protected:

    BASE_T numerical;
    bool tainted;
    // Define a vector to easy access by the non-arithmetic part of the MultipleTaint object
    vector<OTHER_T> character;
    // Define a vector to store the unique element of non-arithmetic part within the MultipleTaint object
    vector<OTHER_T> symbols;

};


#endif //HW0_12_MULTITAINTABLE_H
