package bigjango.babies;

// Work around for setting a ModelBaseMixin field in LivingRenderer
public interface IModelBase {
    public boolean babies$getIsBaby();
    public void babies$setIsBaby(boolean baby);
}
