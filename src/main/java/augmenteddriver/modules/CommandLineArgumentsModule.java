package augmenteddriver.modules;

import com.google.inject.AbstractModule;
import augmenteddriver.util.CommandLineArguments;

/**
 * Guice Module for CommandLineArgumtens.
 *
 * Basically binds CommandLineArguments to the static instance that should have been already initialized.
 */
public class CommandLineArgumentsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandLineArguments.class).toInstance(CommandLineArguments.ARGUMENTS);
    }
}
