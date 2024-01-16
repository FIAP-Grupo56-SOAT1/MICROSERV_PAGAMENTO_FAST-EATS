package br.com.fiap.fasteats.dataprovider.constants;

public class StatusMercadoPagoConstants {
    /**
     * PENDING:      The user has not concluded the payment process (for example, by generating a payment by boleto, it will be concluded at the moment in which the user makes the payment in the selected place).<br>
     * APPROVED:     The payment has been approved and credited.<br>
     * AUTHORIZED:   The payment has been authorized but not captured yet.<br>
     * IN_PROCESS:   The payment is in analysis.<br>
     * IN_MEDIATION: The user started a dispute.<br>
     * REJECTED:     The payment was rejected (the user can try to pay again).<br>
     * CANCELLED:    Either the payment was canceled by one of the parties or expired.<br>
     * REFUNDED:     The payment was returned to the user.<br>
     * CHARGED_BACK: A chargeback was placed on the buyer's credit card.
     *
     * @javadoc
     */
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_AUTHORIZED = "authorized";
    public static final String STATUS_IN_PROCESS = "in_process";
    public static final String STATUS_IN_MEDIATION = "in_mediation";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_REFUNDED = "refunded";
    public static final String STATUS_CHARGED_BACK = "charged_back";

    private StatusMercadoPagoConstants() {
    }
}