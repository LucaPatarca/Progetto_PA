package it.unicam.cs.pa.jbudget105129.view;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.scene.Scene;

import java.io.IOException;

public class ViewModule extends AbstractModule {
    @Override
    protected void configure() {
        super.configure();
    }

    @Provides
    @MainScene
    @Singleton
    protected Scene provideMainScene() throws IOException {
        return TableView.getMainScene();
    }
}
