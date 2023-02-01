//
// Created by [redacted]
//

#ifndef HW0_TAINTABLE_H
#define HW0_TAINTABLE_H

#include <iostream>

/**
 * A generalization of {@link TaintableInt} to any of the standard numeric datatypes.
 * This (templated) class represents a Taintable<BASE_T> base class (BASE_T).  For example,
 * Taintable<int> should be equivalent to the TaintableInt class.
 * @tparam BASE_T The base type.
 */
template <typename BASE_T>
class Taintable {
public:

    // default constructor
    Taintable() : myVal(0), myTaint(false) {}

    // Conversion Constructors
    Taintable(BASE_T val, bool taint) : myVal{val}, myTaint{taint} {}

    /**
     * Constructs an untainted Taintable<BASE_T> from a given BASE_T
     * @param val The BASE_T from which to construct a Taintable
     */
    Taintable(const BASE_T val) : myVal{val}, myTaint{false} {}

    Taintable(Taintable& other): myVal{other.myVal}, myTaint{other.myTaint} {}

    // ... Operators

    Taintable& operator++() {
        myVal++;
        return *this;
    }
    
    Taintable<BASE_T> operator++(int) {
        Taintable<BASE_T> temp = *this;
        ++*this;
        return temp;
    }

    // ... ... informational
/**
   * Dereference
   * @return The integer associated with this Taintable
   */
    const BASE_T operator*() const {
        return myVal;
    }

    const bool operator&() const {
        return myTaint;
    }

    // ... ... assignment
    /**
     * Assignment from another Taintable
     * @param other The value to assign to this Taintable
     * @return The newly assigned value (i.e., other).
     */
    Taintable<BASE_T>& operator=(Taintable<BASE_T>& other) {
        myVal = other.myVal;
        myTaint = other.myTaint;
        return *this;
    }

    /**
     * Assignment from an BASE_T
     * @param other The value to assign to this Taintable
     * @return The newly assigned value (i.e., other), without any taint.
     */
    Taintable<BASE_T>& operator=(BASE_T other) {
        myVal = other;
        myTaint = false;
        return *this;
    }

    // ... ... some arithmetic
    // (arithmetic where the Tainted is a second argument are addressed outside the class
    template <class OP_T> Taintable<BASE_T>& operator+(OP_T second) {
        auto result = std::make_shared<Taintable<BASE_T> >(operator*() + second, operator&());  return *result;
    }
    template <class OP_T> Taintable<BASE_T>& operator-(OP_T second) {
        auto result = std::make_shared<Taintable<BASE_T> >(operator*() - second, operator&());  return *result;
    }
    template <class OP_T> Taintable<BASE_T>& operator*(OP_T second) {
        auto result = std::make_shared<Taintable<BASE_T> >(operator*() * second, operator&());
        return *result;
    }
    template <class OP_T> Taintable<BASE_T>& operator/(OP_T second) {
        auto result = std::make_shared<Taintable<BASE_T> >(operator*() / second, operator&());  return *result;
    }
    template <class OP_T> Taintable<BASE_T>& operator%(OP_T second) {
        auto result = std::make_shared<Taintable<BASE_T> >(operator*() % second, operator&());  return *result;
    }

    // ... ... other

    /**
   * Implicit conversion from object to primitive types
   */
    explicit operator const int() { return operator*(); }
    explicit operator const long() { return operator*(); }
    explicit operator const float() { return operator*(); }
    explicit operator const double() { return operator*(); }

    /**
     * Taint the Taintable<BASE_T> t1.  If t1 is already tainted, it remains tainted.
     * @param ti The Taintable<BASE_T> to taint
     */
    static void taint(Taintable& ti) {
        ti.myTaint=true;
    }

    /**
     * Equality check of two TaintedInts
     * @param lhs The left hand side argument being compared.
     * @param rhs The right hand side argument being compared.
     * @return true iff lhs and rhs have represent the same integer (irrespective of taint).
     */
    friend bool operator==(const Taintable<BASE_T> lhs, const Taintable<BASE_T> rhs) {
        return *lhs == *rhs;
    }

    // informational

    // for displaying
    friend std::ostream &operator<<(std::ostream &os, const Taintable<BASE_T> &item) {
        os << *item;
        if (&item)
            os << " [tainted] ";
        return os;
    }

protected:
    BASE_T myVal;
    bool myTaint;
};

// arithmetic operations, where the first operand is *not* Taintable
template <class BASE_T, class OP_T> Taintable<BASE_T>& operator+(OP_T first, Taintable<BASE_T> second) {
    auto result = std::make_shared<Taintable<BASE_T> >(first + *second, &second); return *result;
}
template <class BASE_T, class OP_T> Taintable<BASE_T>& operator-(OP_T first, Taintable<BASE_T> second) {
    auto result = std::make_shared<Taintable<BASE_T> >(first - *second, &second);  return *result;
}
template <class BASE_T, class OP_T> Taintable<BASE_T>& operator*(OP_T first, Taintable<BASE_T> second) {
   auto result = std::make_shared<Taintable<BASE_T> >(first * *second, &second);
    return *result;
}
template <class BASE_T, class OP_T> Taintable<BASE_T>& operator/(OP_T first, Taintable<BASE_T> second) {
   auto  result = std::make_shared<Taintable<BASE_T> >(first / *second, &second);  return *result;
}
template <class BASE_T, class OP_T> Taintable<BASE_T>& operator%(OP_T first, Taintable<BASE_T> second) {
   auto result = std::make_shared<Taintable<BASE_T> >(first % *second, &second);  return *result;
}

#endif //HW0_TAINTABLE_H
