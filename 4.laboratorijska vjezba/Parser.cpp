#include <iostream>
#include <fstream>
#include <string>
using namespace std;

void A();
void S();
void B();
void C();

string niz;
char ulaz;
int brojac = 0;

void NE() {
    cout << endl << "NE";
    exit(1);
}

void S() {
    cout << "S";
    if(ulaz == 'a') {
        ulaz = niz[brojac++];
        A();
        B();
    } else if (ulaz == 'b') {
        ulaz = niz[brojac++];
        B();
        A();
    } else 
        NE();
}

void A() {
    cout << "A";
    if(ulaz == 'b') {
        ulaz = niz[brojac++];
        C();
    } else if(ulaz == 'a') {
        ulaz = niz[brojac++];
    } else 
        NE();
}

void B() {
    cout << "B";
    if(ulaz == 'c') {
        
        ulaz = niz[brojac++];

        if(ulaz != 'c')
            NE();
        ulaz = niz[brojac++];

        S();

        if(ulaz != 'b')
            NE();
        ulaz = niz[brojac++];

        if(ulaz != 'c')
            NE();
        ulaz = niz[brojac++];
    } 
   
}

void C() {
    cout << "C";
    A();
    A();
}

int main () {

    string ulazniNiz;

    cin >> ulazniNiz;

    niz = ulazniNiz.append("$");
    ulaz = niz[brojac++];

    S();

    if(ulaz == '$')
        cout << endl << "DA";
    else
        cout << endl << "NE";

    return 0;
}