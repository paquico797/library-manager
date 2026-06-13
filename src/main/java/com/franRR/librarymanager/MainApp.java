package com.franRR.librarymanager;

import com.franRR.librarymanager.model.Category;
import com.franRR.librarymanager.repository.impl.CategoryRepository;
import com.franRR.librarymanager.repository.impl.CategoryRepositoryHibernate;
import org.slf4j.helpers.Util;

import java.util.List;

public class MainApp {

    public static void main(String[] args){
        System.out.println(" ARRANCANDO PRUEBA DE CONEXIÓN Y BACKEND ");

        CategoryRepository categoryRepository = new CategoryRepositoryHibernate();
g
        try {
            // 2. Intentamos crear una nueva categoría de prueba
            System.out.println("\n1. Insertando categoría de prueba...");
            Category nuevaCategoria = new Category("Terror");
            categoryRepository.save(nuevaCategoria);
            System.out.println("¡OK! Categoría guardada con éxito.");

            // 3. Intentamos listar todo lo que hay en MySQL para ver si lo lee
            System.out.println("\n2. Recuperando categorías desde MySQL...");
            List<Category> lista = categoryRepository.findAll();

            System.out.println("--- Categorías encontradas en la Base de Datos ---");
            for (Category cat : lista) {
                System.out.println("ID: " + cat.getId() + " | Nombre: " + cat.getName());
            }
            System.out.println("-------------------------------------------------");

        } catch (Exception e) {
            System.err.println("\n❌ ¡ALERTA! Algo ha fallado en la prueba:");
            e.printStackTrace();
        } finally {
            // 4. Pase lo que pase, cerramos la fábrica de conexiones al terminar
            System.out.println("\nApagando el motor de Hibernate...");
            HibernateUtil.shutdown();
            System.out.println("=== 🏁 PRUEBA FINALIZADA ===");
        }
    }

}
