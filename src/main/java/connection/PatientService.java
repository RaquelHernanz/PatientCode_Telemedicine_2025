package connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray; // Necesario para enviar List<Integer> de mediciones
import pojos.Appointment;
import pojos.Doctor;
import pojos.Patient;
import pojos.Measurement; // Para el tipo de retorno en listados
import utilities.Utilities;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList; // Para construir listas



/**
 * Gestiona la comunicación con el servidor para el cliente Paciente[cite: 33].
 */
public class PatientService {
    // clase intermedia entre el menú del paciente y el servidor.
    private static final Gson gson = new Gson();

    private static Patient currentPatient;

    // --- GETTERS & SETTERS ---
    public static Patient getCurrentPatient(){
        return currentPatient;
    } // devuelve el paciente que está usando la app

    public static void setCurrentPatient(Patient p){
        currentPatient = p;
    } //asigna el paciente tras el log in

    // Asegura que el socket está abierto antes de cualquier comunicación
    private static void ensureConnected() throws IOException{
        if (!Connection.isConnected()) {
            Connection.connectToServer();
        }
    }

    /**
     * Envía un REGISTER_PATIENT al servidor.
     * @param patient Objeto Patient con datos (sin ID aún)
     * @param password Contraseña en texto plano
     * @return El objeto Patient con el ID asignado por el servidor, o null si falla.
     */
    public static Patient registerPatient(Patient patient, String password) throws IOException {
        ensureConnected(); // se asegura que este conectado
        String requestId = "pat-reg-" + System.currentTimeMillis(); // genera un request unico

        // 1. Construir Payload (ajustado al Protocolo del Servidor)
        // payload es “el contenido real” que se envía dentro de una petición.

        JsonObject payload = new JsonObject(); // construye el payload, la petición entera
        payload.addProperty("name", patient.getName());
        payload.addProperty("surname", patient.getSurname());
        payload.addProperty("email", patient.getEmail());
        // Enviamos en texto plano. El servidor lo hashea y guarda.
        payload.addProperty("password", password);
        payload.addProperty("dob", patient.getDob());
        payload.addProperty("sex", Utilities.returnSexString(patient.getSex()));
        payload.addProperty("phone", patient.getPhonenumber());

        // 2. Construir Request
        JsonObject request = new JsonObject();
        request.addProperty("type", "REQUEST");
        request.addProperty("action", "REGISTER_PATIENT");
        request.addProperty("role", "PATIENT");
        request.addProperty("requestId", requestId);
        request.add("payload", payload);

        // >>>Incluir el doctor <<<
        Doctor doc = patient.getDoctor();
        if (doc == null) {
            throw new IOException("No doctor assigned to patient before registration.");
        }
        payload.addProperty("doctorId", doc.getId());
        payload.addProperty("doctorEmail", doc.getEmail());
        payload.addProperty("doctorName", doc.getName()); // solo nombre, como fallback

        // 3. Enviar y Leer Respuesta
        String jsonRequest = gson.toJson(request); // convierte el JsonObject a un String JSON
        String jsonResponse = Connection.sendAndRead(jsonRequest); // envía el JSON al servidor y espera la respuesta

        if (jsonResponse == null) {
            throw new IOException("No response from server.");
        }

        // 4. Procesar Respuesta
        JsonObject resp = gson.fromJson(jsonResponse, JsonObject.class); // convierte la respuesta en objeto
        String status = resp.has("status") ? resp.get("status").getAsString() : "ERROR";

        if ("OK".equalsIgnoreCase(status) || "ALL_RIGHT".equalsIgnoreCase(status)) { // respuesta del servidor sobre si fue correcta la operación
            JsonObject respPayload = resp.getAsJsonObject("payload"); // solo coge el payload que son los valores reales
            if (respPayload != null && respPayload.has("patientId")) {
                // Registro exitoso: asignamos el ID devuelto
                patient.setId(respPayload.get("patientId").getAsInt()); // coge el id que le manda el servidor del paciente que se ha registrado
                currentPatient = patient; // asigna al paciente como current patient
                return patient;
            }
        }

        // Si falla, lanzar excepción con el mensaje del servidor
        String msg = resp.has("message") ? resp.get("message").getAsString() : "Unknown error";
        throw new IOException("Registration failed: " + msg);
    }


    /**
     * Envía un LOGIN al servidor.
     * @param email Email del paciente
     * @param password Contraseña en texto plano
     * @return El objeto Patient con el ID y email, o null si falla.
     */
    public static Patient login(String email, String password) throws IOException {
        ensureConnected();
        String requestId = "pat-login-" + System.currentTimeMillis();

        // 1. Construir Payload
        JsonObject payload = new JsonObject();
        payload.addProperty("username", email);
        // Enviamos en texto plano. El servidor lo hashea y compara.
        payload.addProperty("password", password);

        // 2. Construir Request
        JsonObject request = new JsonObject();
        request.addProperty("type", "REQUEST");
        request.addProperty("action", "LOGIN");
        request.addProperty("role", "PATIENT");
        request.addProperty("requestId", requestId);
        request.add("payload", payload);

        // 3. Enviar y Leer Respuesta
        String jsonRequest = gson.toJson(request);
        String jsonResponse = Connection.sendAndRead(jsonRequest);

        if (jsonResponse == null) {
            throw new IOException("No response from server.");
        }

        // 4. Procesar Respuesta
        JsonObject resp = gson.fromJson(jsonResponse, JsonObject.class);
        String status = resp.has("status") ? resp.get("status").getAsString() : "ERROR";

        if ("OK".equalsIgnoreCase(status) || "ALL_RIGHT".equalsIgnoreCase(status)) {
            JsonObject respPayload = resp.getAsJsonObject("payload");

            if (respPayload != null && respPayload.has("userId") && respPayload.has("role") && "PATIENT".equals(respPayload.get("role").getAsString())) {
                // Login exitoso: creamos un Patient parcial para la sesión
                Patient patient = new Patient();
                patient.setId(respPayload.get("userId").getAsInt());
                patient.setEmail(email);
                currentPatient = patient;
                //System.out.println("doctor del paciente" + currentPatient.getDoctor());
                return patient;
            }
        }

        // Si falla, lanzar excepción con el mensaje del servidor
        String msg = resp.has("message") ? resp.get("message").getAsString() : "Unknown error";
        throw new IOException("Login failed: " + msg);
    }

    /**
     * Envía un SEND_SYMPTOMS al servidor.
     * @param description Texto de los síntomas
     * @return true si el servidor confirma el almacenamiento.
     */
    public static boolean sendSymptoms(String description) throws IOException {
        if (currentPatient == null || currentPatient.getId() <= 0) {
            throw new IllegalStateException("Patient not logged in.");
        }
        ensureConnected();
        String requestId = "pat-symp-" + System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();

        // 1. Construir Payload
        JsonObject payload = new JsonObject();
        payload.addProperty("patientId", currentPatient.getId());
        payload.addProperty("description", description);
        payload.addProperty("hour", Utilities.formatDateTime(now)); // ISO 8601

        // 2. Construir Request
        JsonObject request = new JsonObject();
        request.addProperty("type", "REQUEST");
        request.addProperty("action", "SEND_SYMPTOMS");
        request.addProperty("role", "PATIENT");
        request.addProperty("requestId", requestId);
        request.add("payload", payload);

        // 3. Enviar y Leer Respuesta
        String jsonRequest = gson.toJson(request);
        String jsonResponse = Connection.sendAndRead(jsonRequest);

        if (jsonResponse == null) {
            throw new IOException("No response from server.");
        }

        // 4. Procesar Respuesta
        JsonObject resp = gson.fromJson(jsonResponse, JsonObject.class);
        String status = resp.has("status") ? resp.get("status").getAsString() : "ERROR";

        if ("OK".equalsIgnoreCase(status) || "ALL_RIGHT".equalsIgnoreCase(status)) {
            System.out.println("[PatientService] Symptoms stored successfully.");
            return true;
        }

        String msg = resp.has("message") ? resp.get("message").getAsString() : "Unknown error";
        throw new IOException("Send Symptoms failed: " + msg);
    }

    /**
     * Lista todos los doctores disponibles.
     * @return Lista de objetos Doctor (parciales: solo id, nombre, email).
     */
    public static List<Doctor> listAllDoctors() throws IOException {
        JsonObject pl = call("LIST_DOCTORS", new JsonObject());

        List<Doctor> out = new ArrayList<>();
        if (pl.has("doctors") && pl.get("doctors").isJsonArray()) {
            for (com.google.gson.JsonElement el : pl.getAsJsonArray("doctors")) {
                JsonObject d = el.getAsJsonObject();
                Doctor doc = new Doctor();
                if (d.has("doctorId")) doc.setId(d.get("doctorId").getAsInt());
                if (d.has("name")) doc.setName(d.get("name").getAsString());
                if (d.has("surname")) doc.setSurname(d.get("surname").getAsString());
                if (d.has("email")) doc.setEmail(d.get("email").getAsString());
                out.add(doc);
            }
        }
        return out;
    }



    /**
     * Envía un REQUEST_APPOINTMENT al servidor.
     */
   public static int requestAppointment(int doctorId, String datetimeIso, String message) throws IOException {
        if (currentPatient == null) {
            throw new IllegalStateException("Patient not logged in.");
        }
       java.util.List<pojos.Appointment> appointments = listAppointmentsForDoctor(doctorId);
        for (pojos.Appointment appointment : appointments) {
            if(appointment.getDate().equals(datetimeIso)) {
                throw new IllegalStateException("Appointment not available. Choose another hour or date.");
            }
        }

        JsonObject payload = new JsonObject();
        payload.addProperty("doctorId", doctorId);
        payload.addProperty("patientId", currentPatient.getId());
        payload.addProperty("datetime", datetimeIso);
        payload.addProperty("message", message);

        JsonObject pl = call("REQUEST_APPOINTMENT", payload);

        if (pl.has("appointmentId")) {
            return pl.get("appointmentId").getAsInt();
        }
        throw new IOException("Server did not return appointment ID.");
    }

    public static java.util.List<pojos.Appointment> listAppointmentsForDoctor(int doctorId) throws IOException {
        JsonObject payload = new JsonObject();
        payload.addProperty("doctorId", doctorId);

        JsonObject pl = call("LIST_APPOINTMENTS", payload);

        java.util.List<pojos.Appointment> out = new java.util.ArrayList<>();
        if (pl.has("appointments") && pl.get("appointments").isJsonArray()) {
            for (com.google.gson.JsonElement el : pl.getAsJsonArray("appointments")) {
                JsonObject a = el.getAsJsonObject();
                pojos.Appointment ap = new pojos.Appointment();
                if (a.has("id")) ap.setId(a.get("id").getAsInt());
                if (a.has("message")) ap.setMessage(a.get("message").getAsString());
                ap.setDate(readLdt(a, "date", "dateTime"));

                if (a.has("doctorId") && !a.get("doctorId").isJsonNull()) {
                    pojos.Doctor d = new pojos.Doctor(); d.setId(a.get("doctorId").getAsInt()); ap.setDoctor(d);
                } else { pojos.Doctor d = new pojos.Doctor(); d.setId(doctorId); ap.setDoctor(d); }

                if (a.has("patientId") && !a.get("patientId").isJsonNull()) {
                    pojos.Patient p = new pojos.Patient(); p.setId(a.get("patientId").getAsInt()); ap.setPatient(p);
                }
                out.add(ap);
            }
        }
        return out;
    }
    /* Util para fechas, aceptando varios nombres de campo del server */
    private static java.time.LocalDateTime readLdt(JsonObject o, String... keys) {
        for (String k : keys) {
            if (o.has(k) && !o.get(k).isJsonNull()) {
                java.time.LocalDateTime dt = utilities.Utilities.parseDateTime(o.get(k).getAsString());
                if (dt != null) return dt;
            }
        }
        // fallback (date + hour)
        if (o.has("date") && o.has("hour")) {
            return utilities.Utilities.parseDateTime(o.get("date").getAsString() + "T" + o.get("hour").getAsString());
        }
        return null;
    }



    /**
     * Envía un SEND_MESSAGE al doctor.
     */
    public static boolean sendMessage(int doctorId, String text) throws IOException {
        if (currentPatient == null) {
            throw new IllegalStateException("Patient not logged in.");
        }
        JsonObject payload = new JsonObject();
        payload.addProperty("doctorId", doctorId);
        payload.addProperty("patientId", currentPatient.getId());
        payload.addProperty("senderRole", "PATIENT");
        payload.addProperty("text", text);

        call("SEND_MESSAGE", payload);
        return true; // Si no hay excepción, fue exitoso.
    }

    // --- BASE DE COMUNICACIÓN (similar a DoctorService) ---

    private static boolean isOk(JsonObject resp){
        if (resp == null || !resp.has("status")) return false;
        String s = resp.get("status").getAsString();
        // acepta tanto "OK" como "All_RIGHT" del servidor
        return "OK".equalsIgnoreCase(s) || "ALL_RIGHT".equalsIgnoreCase(s);
    }

    /**
     * Envía una REQUEST genérica y gestiona la respuesta y errores.
     *
     * @param action  Acción del protocolo (e.g., "LIST_DOCTORS")
     * @param payload Carga JSON con datos de la petición
     * @return El 'payload' de la respuesta OK o lanza IOException si falla.
     */
    private static JsonObject call(String action, JsonObject payload) throws IOException{
        ensureConnected();
        String requestId = action.toLowerCase() + "-" + System.currentTimeMillis();

        JsonObject req = new JsonObject();
        req.addProperty("type", "REQUEST");
        req.addProperty("action", action);
        req.addProperty("role", "PATIENT");
        req.addProperty("requestId", requestId);
        if (payload != null) req.add("payload", payload);

        String jsonRequest = gson.toJson(req);
        String jsonResponse = Connection.sendAndRead(jsonRequest);

        if (jsonResponse == null) {
            throw new IOException("No response from server.");
        }

        JsonObject resp = gson.fromJson(jsonResponse, JsonObject.class);
        if (!isOk(resp)) {
            String msg = resp.has("message") ? resp.get("message").getAsString() : "Unknown error";
            throw new IOException(action + " failed: " + msg);
        }
        return resp.has("payload") && resp.get("payload").isJsonObject()
                ? resp.getAsJsonObject("payload")
                : new JsonObject();
    }

    public static List<String> listConversationWithDoctor(int doctorId) throws IOException {
        if (currentPatient == null || currentPatient.getId() <= 0) {
            throw new IllegalStateException("Patient not logged in.");
        }

        JsonObject payload = new JsonObject();
        payload.addProperty("doctorId", doctorId);
        payload.addProperty("patientId", currentPatient.getId());

        JsonObject pl = call("LIST_MESSAGES", payload);

        List<String> messages = new ArrayList<>();
        if (pl.has("messages") && pl.get("messages").isJsonArray()) {
            for (com.google.gson.JsonElement el : pl.getAsJsonArray("messages")) {
                JsonObject m = el.getAsJsonObject();
                String sender = m.has("senderRole") ? m.get("senderRole").getAsString() : "?";
                String text   = m.has("text") ? m.get("text").getAsString() : "";
                String ts     = m.has("timestamp") ? m.get("timestamp").getAsString() : "";
                messages.add("[" + ts + "] " + sender + ": " + text);
            }
        }
        return messages;
    }

    public static boolean sendMeasurements(int doctorId, List<Integer> data, String type) throws IOException {
        if (currentPatient == null) {
            throw new IllegalStateException("Patient not logged in.");
        }
        LocalDateTime date = LocalDateTime.now();

        JsonObject payload = new JsonObject();

        JsonArray values = new JsonArray();
        for (Integer v : data) {
            values.add(v);
        }
        payload.addProperty("doctorId", doctorId);
        payload.addProperty("patientId", currentPatient.getId());
        payload.addProperty("type", type);
        payload.addProperty("date", date.toString()); // ISO 8601
        payload.add("values", values);


        call("SEND_MEASUREMENT", payload);
        return true; // Si no hay excepción, fue exitoso.
    }

}