package com.example.BazaarTrackerBackend.repository.user;

import com.example.BazaarTrackerBackend.model.entity.User;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import com.google.cloud.firestore.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION = CollectionNames.USERS;

    // ✅ SAVE USER
    public User save(User user) throws ExecutionException, InterruptedException {

        DocumentReference docRef;

        if (user.getId() == null) {
            docRef = firestore.collection(COLLECTION).document();
            user.setId(docRef.getId());
        } else {
            docRef = firestore.collection(COLLECTION).document(user.getId());
        }

        docRef.set(user).get();

        return user;
    }

    // ✅ FIND BY EMAIL (VERY IMPORTANT)
    public User findByEmail(String email) throws ExecutionException, InterruptedException {

        Query query = firestore.collection(COLLECTION)
                .whereEqualTo("email", email)
                .limit(1);

        QuerySnapshot querySnapshot = query.get().get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        if (documents.isEmpty()) {
            return null;
        }

        return documents.get(0).toObject(User.class);
    }
}