package com.example.progettopersone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    // Dichiarazione di alcune variabili di classe
    private lateinit var db: AppDatabase
    private lateinit var dao: PersonaDAO
    private var isAddPersonaVisible = false

    // Dichiarazione di alcuni elementi dell'interfaccia utente
    lateinit var button_aggiungi_persona: Button
    lateinit var edittext_nome: EditText
    lateinit var edittext_cognome: EditText
    lateinit var edittext_data_nascita: EditText
    lateinit var edittext_sesso: EditText
    lateinit var edittext_citta_nascita: EditText
    lateinit var edittext_provincia_nascita: EditText
    lateinit var rv_people: RecyclerView
    lateinit var add_persona_section: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inizializzazione degli elementi dell'interfaccia utente
        button_aggiungi_persona = findViewById(R.id.button_aggiungi_persona)
        edittext_nome = findViewById(R.id.edittext_nome)
        edittext_cognome = findViewById(R.id.edittext_cognome)
        edittext_data_nascita = findViewById(R.id.edittext_data_nascita)
        edittext_sesso = findViewById(R.id.edittext_sesso)
        edittext_citta_nascita = findViewById(R.id.edittext_citta_nascita)
        edittext_provincia_nascita = findViewById(R.id.edittext_provincia_nascita)
        rv_people = findViewById(R.id.rv_people)
        add_persona_section = findViewById(R.id.layout_aggiungi_persona)
        rv_people.layoutManager = LinearLayoutManager(this)

        // Inizializzazione del database e del DAO
        db = AppDatabase.getDatabase(this)
        dao = db.personaDAO()

        // Mostra tutte le persone presenti nel database all'avvio dell'app
        showAllPeople()

        // Aggiungi la persona al database quando si clicca sul pulsante "Aggiungi"
        button_aggiungi_persona.setOnClickListener {
            addPersona()
        }
    }

    // Metodo per aggiungere una persona al database
    private fun addPersona() {

        // Verifica se la sezione di aggiunta persona è visibile e aggiorna la variabile di stato
        if (isAddPersonaVisible) {

            // Recupera i valori inseriti dall'utente
            val nome = edittext_nome.text.toString().trim()
            val cognome = edittext_cognome.text.toString().trim()
            val dataNascita = edittext_data_nascita.text.toString().trim()
            val sesso = edittext_sesso.text.toString().trim()
            val cittaNascita = edittext_citta_nascita.text.toString().trim()
            val provinciaNascita = edittext_provincia_nascita.text.toString().trim()

            // Verifica che tutti i campi siano stati inseriti
            if (nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty() || sesso.isEmpty() || cittaNascita.isEmpty() || provinciaNascita.isEmpty()) {
                Toast.makeText(this, "Inserire tutti i campi", Toast.LENGTH_SHORT).show()
                return
            }

            // Crea una nuova istanza di Persona con i valori inseriti dall'utente
            val person = Persona(null, nome, cognome, dataNascita, sesso, cittaNascita, provinciaNascita)
            // Aggiungi la persona al database in modo asincrono
            GlobalScope.launch {
                dao.insert(person)
                showAllPeople()
                // Cambia il testo del pulsante ad "+"
                runOnUiThread { button_aggiungi_persona.text = "+" }
            }

            // Resetta i campi di input
            resetInputFields()

            // Mostra un messaggio di conferma
            Toast.makeText(this, "Persona aggiunta al database", Toast.LENGTH_SHORT).show()
            hideAddPersona()

        } else {
            // Cambia il testo del pulsante ad "Aggiungi persona"
            button_aggiungi_persona.text = "Aggiungi persona"
            showAddPersona()
        }

    }


    // Metodo per mostrare la sezione di aggiunta persona
    private fun showAddPersona() {
        add_persona_section.visibility = View.VISIBLE
        isAddPersonaVisible = true
    }

    // Metodo per nascondere la sezione di aggiunta persona
    private fun hideAddPersona() {
        add_persona_section.visibility = View.GONE
        isAddPersonaVisible = false
    }

    // Metodo per mostrare tutte le persone presenti nel database
    private fun showAllPeople() {
        GlobalScope.launch {
            val people = dao.getAll()

            // Aggiorna l'interfaccia utente sulla UI Thread
            runOnUiThread {
                val adapter = PersonaAdapter(people)
                rv_people.adapter = adapter
            }
        }
    }

    // Metodo per resettare i campi di input
    private fun resetInputFields() {
        edittext_nome.setText("")
        edittext_cognome.setText("")
        edittext_data_nascita.setText("")
        edittext_sesso.setText("")
        edittext_citta_nascita.setText("")
        edittext_provincia_nascita.setText("")
    }
}

// Adapter per la RecyclerView
class PersonaAdapter(private val people: List<Persona>) : RecyclerView.Adapter<PersonaAdapter.ViewHolder>() {
    // Crea la vista per ogni elemento della RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.persona_item, parent, false)
        return ViewHolder(view)
    }

    // Aggiorna i dati nella vista per ogni elemento della RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]

        holder.nome.text = person.nome
        holder.cognome.text = person.cognome
        holder.dataNascita.text = person.dataNascita
        holder.sesso.text = person.sesso
        holder.cittaNascita.text = person.cittaNascita
        holder.provinciaNascita.text = person.provinciaNascita
    }

    // Restituisce il numero di elementi nella RecyclerView
    override fun getItemCount(): Int {
        return people.size
    }

    // ViewHolder per ogni elemento della RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nome: TextView = itemView.findViewById(R.id.nome)
        val cognome: TextView = itemView.findViewById(R.id.cognome)
        val dataNascita: TextView = itemView.findViewById(R.id.data_nascita)
        val sesso: TextView = itemView.findViewById(R.id.sesso)
        val cittaNascita: TextView = itemView.findViewById(R.id.citta_nascita)
        val provinciaNascita: TextView = itemView.findViewById(R.id.provincia_nascita)
    }
}




