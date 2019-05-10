package nl.tudelft.simulation.dsol.hla;

public final class Barrier
{
    /** is the barrier lowered ? */
    private boolean lowered;

    /** the purpose of the barrier. */
    private String purpose = "";

    /**
     * constructs a new Barrier.
     */
    public Barrier()
    {
        super();
    }

    /**
     * lowers the barrier
     * @param returnedValues
     */
    public synchronized void lower()
    {
        this.lowered = true;
        this.purpose = "";
        // awaken waiters
        this.notifyAll();
    }

    /**
     * awaits the barrier
     * @return returns the returnValues
     */
    public synchronized void raise(final String purpose)
    {
        this.purpose = purpose;
        while (!this.lowered)
        {
            try
            {
                this.wait();
            }
            catch (InterruptedException e)
            {
                // noting to do; we may continue
            }
        }
        this.lowered = false; // semantics are one-shot
    }

    /**
     * @return Returns the lowered.
     */
    public boolean isLowered()
    {
        return this.lowered;
    }

    /**
     * @return Returns the purpose.
     */
    public String getPurpose()
    {
        return this.purpose;
    }
}
