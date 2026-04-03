package com.example.BazaarTrackerBackend.repository.firestore;

import com.google.cloud.firestore.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class FirestoreRepository {

    @Autowired
    private Firestore firestore;

    // ✅ SAVE (Create or Update)
    public <T> T save(String collectionName, T entity) throws ExecutionException, InterruptedException {

        DocumentReference docRef;

        // Try to get "id" field using reflection
        try {
            String id = (String) entity.getClass().getMethod("getId").invoke(entity);

            if (id == null) {
                docRef = firestore.collection(collectionName).document();
                entity.getClass().getMethod("setId", String.class)
                        .invoke(entity, docRef.getId());
            } else {
                docRef = firestore.collection(collectionName).document(id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error handling entity ID", e);
        }

        docRef.set(entity).get();

        return entity;
    }

    // ✅ FIND BY ID
    public <T> T findById(String collectionName, String id, Class<T> clazz)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot snapshot = firestore
                .collection(collectionName)
                .document(id)
                .get()
                .get();

        if (!snapshot.exists()) {
            return null;
        }

        return snapshot.toObject(clazz);
    }

    // ✅ FIND ALL
    public <T> List<T> findAll(String collectionName, Class<T> clazz)
            throws ExecutionException, InterruptedException {

        List<QueryDocumentSnapshot> documents = firestore
                .collection(collectionName)
                .get()
                .get()
                .getDocuments();

        return documents.stream()
                .map(doc -> doc.toObject(clazz))
                .collect(Collectors.toList());
    }

    // ✅ FIND BY FIELD
    public <T> List<T> findByField(String collectionName, String field, String value, Class<T> clazz)
            throws ExecutionException, InterruptedException {

        List<QueryDocumentSnapshot> documents = firestore
                .collection(collectionName)
                .whereEqualTo(field, value)
                .get()
                .get()
                .getDocuments();

        return documents.stream()
                .map(doc -> doc.toObject(clazz))
                .collect(Collectors.toList());
    }

    // ✅ DELETE
    public void delete(String collectionName, String id)
            throws ExecutionException, InterruptedException {

        firestore.collection(collectionName)
                .document(id)
                .delete()
                .get();
    }
}