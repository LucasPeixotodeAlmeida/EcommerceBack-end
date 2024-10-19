package com.lucas.ecommerce.config;

import com.lucas.ecommerce.entities.ProductCategoryEntity;
import com.lucas.ecommerce.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


//A classe MyDataRestConfig implementa a interface RepositoryRestConfigurer, que é usada para personalizar a configuração do Spring Data REST.
//Esse tipo de configuração permite ajustar como os dados das entidades são expostos via endpoints RESTFUL.
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    //O EntityManager é usado para gerenciar as entidades do JPA (Java Persistence API). Ele permite interagir com o banco de dados e acessar o metamodelo de todas as entidades mapeadas,
    //como o ProductEntity e ProductCategoryEntity.
    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    //Esse metodo sobrescreve o comportamento padrão de como as entidades são expostas via REST no Spring Data REST.
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        //Lista de métodos HTTP que deseja desativar para evitar alterações diretas nos dados. Os métodos PUT, POST e DELETE são desativados para impedir que os clientes criem,
        // atualizem ou deletem entidades diretamente via API REST.
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST};

        // Desabilitar PUT, POST e DELETE para a entidade ProductEntity
        config.getExposureConfiguration()
                //.withItemExposure controla como um único recurso (por exemplo, um produto individual) pode ser exposto.
                //.withCollectionExposure controla como a coleção de recursos (uma lista de produtos) pode ser exposta.
                .forDomainType(ProductEntity.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // Desabilitar PUT, POST e DELETE para a entidade ProductCategoryEntity
        config.getExposureConfiguration()
                //.withItemExposure controla como um único recurso (por exemplo, um produto individual) pode ser exposto.
                //.withCollectionExposure controla como a coleção de recursos (uma lista de produtos) pode ser exposta.
                .forDomainType(ProductCategoryEntity.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //Por padrão, o Spring Data REST não expõe os IDs das entidades nos resultados das APIs, por questões de segurança e boas práticas.
        //exposeIds é um metodo auxiliar que faz o trabalho de expor os IDs das entidades no JSON retornado.
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config){

        //obtém todas as entidades mapeadas no EntityManager. O metodo getMetamodel().getEntities() retorna um conjunto (Set) de todas as entidades que estão registradas no JPA.
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //cria uma lista para armazenar as classes Java dessas entidades
        List<Class> entityClasses = new ArrayList<>();

        //itera sobre todas as entidades para adicionar suas classes Java na lista
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        //converte em um array e chama o metodo exposeIdsFor, que informa ao Spring Data REST para expor os IDs dessas entidades.
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
