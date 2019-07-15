package insat.gl.recipies.services;

import java.util.Set;

import insat.gl.recipies.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
