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

    private lateinit var db: AppDatabase
    private lateinit var dao: PersonaDAO

    lateinit var button_aggiungi_persona: Button
    lateinit var edittext_nome: EditText
    lateinit var edittext_cognome: EditText
    lateinit var edittext_data_nascita: EditText
    lateinit var edittext_sesso: EditText
    lateinit var edittext_citta_nascita: EditText
    lateinit var edittext_provincia_nascita: EditText
    lateinit var rv_people: RecyclerView
    private var isAddPersonaVisible = false
    lateinit var add_persona_section: LinearLayout


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            button_aggiungi_persona =findViewById<Button>(R.id.button_aggiungi_persona)
            edittext_nome = findViewById<EditText>(R.id.edittext_nome)
            edittext_cognome = findViewById<EditText>(R.id.edittext_cognome)
            edittext_data_nascita = findViewById<EditText>(R.id.edittext_data_nascita)
            edittext_sesso = findViewById<EditText>(R.id.edittext_sesso)
            edittext_citta_nascita = findViewById<EditText>(R.id.edittext_citta_nascita)
            edittext_provincia_nascita = findViewById<EditText>(R.id.edittext_provincia_nascita)
            add_persona_section = findViewById<LinearLayout>(R.id.layout_aggiungi_persona)
            rv_people = findViewById<RecyclerView>(R.id.rv_people)
            rv_people.layoutManager = LinearLayoutManager(this)



        db = AppDatabase.getDatabase(this)
        dao = db.personaDAO()

        // Mostra tutte le persone presenti nel database all'avvio dell'app
        showAllPeople()

        // Aggiungi la persona al database quando si clicca sul pulsante "Aggiungi"
        button_aggiungi_persona.setOnClickListener {
            addPersona()
        }

    }

    private fun addPersona() {
        if (isAddPersonaVisible) {
            // Se la sezione di aggiunta persona è visibile, nascondila
            hideAddPersona()
        } else {
            // Altrimenti, rendila visibile
            showAddPersona()
        }
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

        // Crea una nuova istanza di Persona
        val person = Persona(null, nome, cognome, dataNascita, sesso, cittaNascita, provinciaNascita)

        // Aggiungi la persona al database
        GlobalScope.launch {
            dao.insert(person)
            showAllPeople()
        }

        // Resetta i campi di input
        resetInputFields()

        // Mostra un messaggio di conferma
        Toast.makeText(this, "Persona aggiunta al database", Toast.LENGTH_SHORT).show()

        // Controlla lo stato della variabile di stato e agisci di conseguenza

    }

    private fun showAddPersona() {
        // Rendi visibile la sezione di aggiunta persona e aggiorna lo stato della variabile di stato
        add_persona_section.visibility = View.VISIBLE
        isAddPersonaVisible = true
    }

    private fun hideAddPersona() {
        // Nascondi la sezione di aggiunta persona e aggiorna lo stato della variabile di stato
        add_persona_section.visibility = View.GONE
        isAddPersonaVisible = false
    }

    private fun showAllPeople() {
        // Recupera tutte le persone presenti nel database
        GlobalScope.launch {
            val people = dao.getAll()

            // Aggiungi le persone alla RecyclerView
            runOnUiThread {
                val adapter = PersonaAdapter(people)
                rv_people.adapter = adapter
            }
        }
    }

    private fun resetInputFields() {
        edittext_nome.setText("")
        edittext_cognome.setText("")
        edittext_data_nascita.setText("")
        edittext_sesso.setText("")
        edittext_citta_nascita.setText("")
        edittext_provincia_nascita.setText("")
    }
}
class PersonaAdapter(private val people: List<Persona>) : RecyclerView.Adapter<PersonaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.persona_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = people[position]

        // Imposta i valori delle viste nella vista dell'elemento della RecyclerView
        holder.nome.text = person.nome
        holder.cognome.text = person.cognome
        holder.dataNascita.text = person.dataNascita
        holder.sesso.text = person.sesso
        holder.cittaNascita.text = person.cittaNascita
        holder.provinciaNascita.text = person.provinciaNascita
    }

    override fun getItemCount(): Int {
        return people.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nome: TextView = itemView.findViewById(R.id.nome)
        val cognome: TextView = itemView.findViewById(R.id.cognome)
        val dataNascita: TextView = itemView.findViewById(R.id.data_nascita)
        val sesso: TextView = itemView.findViewById(R.id.sesso)
        val cittaNascita: TextView = itemView.findViewById(R.id.citta_nascita)
        val provinciaNascita: TextView = itemView.findViewById(R.id.provincia_nascita)
    }
}


